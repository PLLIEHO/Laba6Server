package server;

import server.core.Collection;
import server.core.commands.Hasher;
import server.data.HumanBeing;
import server.data.Mood;
import server.data.WeaponType;

import java.sql.*;

public class DBConnection {
    public static Connection connection;
    private Statement statement;
    public void getConnection() {
        String url = "jdbc:postgresql://localhost:5432/studs";
        String user = "postgres";
        String password = "bee083";
        try{
            connection = DriverManager.getConnection(url, user, password);
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    public String getUserByLogin(String login){
        try {
            statement = connection.createStatement();
            String object = "select * FROM users WHERE UserLogin = '" + login + "'";
            ResultSet rs = statement.executeQuery(object);
            rs.next();
            return rs.getString("userpassword");
        } catch (SQLException | NullPointerException e){
            Server.LOG.info("No such user found.");
            return null;
        }
    }

    public String register(String login, String password){
        try {
            String object = "INSERT INTO "
                    + " users(userlogin, userpassword) "
                    + "values(?, ?)";
            PreparedStatement prepStat = connection.prepareStatement(object);
            prepStat.setString(1,login);
            prepStat.setString(2, new Hasher().hash(password));
            prepStat.executeUpdate();
            return "Registered successfully!";
        } catch (SQLException e){
            return "This login was already taken. Try again";
        }
    }

    public Collection load(){
        Collection collection = new Collection();
        try {
            statement = connection.createStatement();
            ResultSet set = statement.executeQuery("select * FROM HumanBeing");
            while(set.next()){
                HumanBeing human = new HumanBeing();
                human.setId(set.getLong("ID"));
                human.setName(set.getString("name"));
                human.setCreationDate(set.getTimestamp("creationdate"));
                human.setCoordinatesX(set.getFloat("coordsx"));
                human.setCoordinatesY(set.getDouble("coordsy"));
                human.setRealHero(set.getBoolean("realhero"));
                human.setHasToothPick(set.getBoolean("hastoothpick"));
                human.setImpactSpeed(set.getLong("impactspeed"));
                human.setWeaponType(WeaponType.parseWeaponType(set.getString("weapon")));
                human.setMood(Mood.parseMood(set.getString("mood")));
                human.setCarName(set.getString("car_name"));
                human.setCarCool(set.getBoolean("car_coolness"));
                human.setUser(set.getString("userlogin"));
                collection.addHuman(human, collection);
            }
        } catch (SQLException e){
            Server.LOG.info("Caught Database exception. May be some problems with connection.");
        } catch (NullPointerException n){
            Server.LOG.info("Table is empty. Continuing work.");
        }
        return collection;
    }

    public boolean addToBD(HumanBeing human, String user){
        try {
            String object = "INSERT INTO "
                    + " HumanBeing(name, creationdate, coordsX, coordsY, realHero, hasToothpick, impactSpeed, weapon, mood, car_name, car_coolness, userlogin) "
                    + "values(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
            PreparedStatement prepStat = connection.prepareStatement(object);
            addOrUpdateHelp(human, prepStat);
            prepStat.setString(12, user);
            prepStat.executeUpdate();
            return true;
        } catch (SQLException e){
            Server.LOG.info("Caught Database exception. May be some problems with connection.");
            return false;
        }
    }
    public long getID()  {
        try {
            statement = connection.createStatement();
            ResultSet set = statement.executeQuery("select * FROM HumanBeing ORDER BY ID DESC");
            set.next();
            return set.getInt("ID");
        } catch (SQLException e){
            Server.LOG.info("Caught Database exception. May be some problems with connection.");
            return -1;
        }
    }
    public void updateDB(HumanBeing human, String user) {
        try {
            PreparedStatement prepStat = connection.prepareStatement("UPDATE HumanBeing SET name = ?, creationDate = ?, coordsX = ?, coordsY = ?, " +
                    "realhero = ?, hastoothpick = ?, impactspeed = ?, weapon = ?, mood = ?, car_name = ?, car_coolness = ? " +
                    "WHERE (id = ?) AND (userlogin = ?);");
            addOrUpdateHelp(human, prepStat);
            prepStat.setLong(12, human.getId());
            prepStat.setString(13, user);
            prepStat.executeUpdate();
        } catch (SQLException e){
            Server.LOG.info("Caught the SQLException. Strange one, may be some problems with connection.");
        }
    }

    private void addOrUpdateHelp(HumanBeing human, PreparedStatement prepStat) throws SQLException {
        prepStat.setString(1, human.getName());
        prepStat.setTimestamp(2, new Timestamp(human.getCreationDate().getTime()));
        prepStat.setFloat(3, human.getCoordinatesX());
        prepStat.setDouble(4, human.getCoordinatesY());
        prepStat.setBoolean(5, human.getRealHero());
        prepStat.setBoolean(6, human.getHasToothPick());
        prepStat.setLong(7, human.getImpactSpeed());
        prepStat.setString(8, human.getWeaponType().toString());
        prepStat.setString(9, human.getMood().toString());
        prepStat.setString(10, human.getCarName());
        prepStat.setBoolean(11, human.getCarCool());
    }

    public void removeFromBD(HumanBeing humanBeing, String user){
        try {
            PreparedStatement prepState = connection.prepareStatement("DELETE FROM HumanBeing WHERE (ID = ?) AND (userlogin = ?)");
            prepState.setLong(1, humanBeing.getId());
            prepState.setString(2, user);

            prepState.execute();
        } catch (SQLException e){
            Server.LOG.info("No such element found.");
        }
    }
    public void clearDB(String user){
        try{
            PreparedStatement prepState = connection.prepareStatement("DELETE FROM HumanBeing WHERE (userlogin = ?)");
            prepState.setString(1, user);

            prepState.execute();
        } catch (SQLException e){
            Server.LOG.info("No such elements in table.");
        }
    }
}
