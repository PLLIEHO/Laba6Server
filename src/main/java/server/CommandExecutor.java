package server;


import common.CommandList;
import common.Pack;
import common.Request;
import server.core.Collection;
import server.core.commands.*;

import java.io.IOException;
import java.nio.channels.DatagramChannel;
import java.util.concurrent.RecursiveTask;
import java.util.regex.Pattern;

public class CommandExecutor extends RecursiveTask<Object> {
    private Request request;
    private String answer = "";
    private Collection collection;
    private final Pattern enter = Pattern.compile("\n");
    private DatagramChannel channel;
    private History history;

    public CommandExecutor(Object object, Collection collection, DatagramChannel channel, History history){
        this.request = (Request) object;
        this.collection = collection;
        this.channel = channel;
        this.history = history;
    }
    @Override
    protected Object compute() {
        CommandList command = request.getCommand();
        if ((command.equals(CommandList.SIGN_IN)||command.equals(CommandList.SIGN_UP))||(new Hasher().hash(request.getPassword()).equals(new DBConnection().getUserByLogin(request.getUserLogin())))) {
            String user = request.getUserLogin();
            history.storyAdd(command);
            Pack arg = request.getArgument();
            switch (command) {
                case HELP:
                    answer = "help : вывести справку по доступным командам \n" +
                            "info : вывести в стандартный поток вывода информацию о коллекции (тип, дата инициализации, количество элементов и т.д.)\n" +
                            "show : вывести в стандартный поток вывода все элементы коллекции в строковом представлении \n" +
                            "add {element} : добавить новый элемент в коллекцию \n" +
                            "update id {element} : обновить значение элемента коллекции, id которого равен заданному \n" +
                            "remove_by_id id : удалить элемент из коллекции по его id \n" +
                            "clear : очистить коллекцию \n" +
                            "save : сохранить коллекцию в файл \n" +
                            "execute_script file_name : считать и исполнить скрипт из указанного файла. В скрипте содержатся команды в таком же виде, в котором их вводит пользователь в интерактивном режиме. \n" +
                            "exit : завершить программу (без сохранения в файл) \n" +
                            "add_if_max {element} : добавить новый элемент в коллекцию, если его значение превышает значение наибольшего элемента этой коллекции. Доступные для сравнения элементы: \n" +
                            "coordinates_x, coordinates_y, realhero, hastoothpick, impactspeed, carcool; \n" +
                            "remove_greater {element} : удалить из коллекции все элементы, превышающие заданный \n" +
                            "history : вывести последние 9 команд (без их аргументов) \n" +
                            "max_by_real_hero : вывести любой объект из коллекции, значение поля realHero которого является максимальным \n" +
                            "filter_contains_name name : вывести элементы, значение поля name которых содержит заданную подстроку \n" +
                            "print_descending : вывести элементы коллекции в порядке убывания";
                    break;
                case INFO:
                    answer = "Тип коллекции: ArrayDeque \n" + "Текущий размер коллекции: " + collection.getCollection().size() + "\n" + "Дата инициализации: " + collection.getData().toString();

                    break;
                case ADD:
                    String[] values = arg.getArgA().toArray(new String[9]);
                    AddCore add = new AddCore(collection, this, values, user);
                    add.fork();
                    answer = add.join();

                    break;
                case UPDATE:
                    UpdateCore update = new UpdateCore(collection, arg.getArgB(), arg.getArgA());
                    answer = update.update(user);

                    break;
                case SHOW:
                    answer = "ID; Name; CoordsX; CoordsY; CreationDate; isRealHero; hasToothpick; impactSpeed; Weapon; Mood; car_name; car_cool \n";
                    collection.getCollection().forEach(human -> {
                        answer += human.toString();
                    });

                    break;
                case CLEAR:
                    new DBConnection().clearDB(user);
                    collection.clearColl(user, collection);
                    answer = "Cleared successfully";
                    break;
                case EXIT:
                    Server.LOG.info("Client has disconnected.");
                    answer = "Goodbye, " + user;
                    break;
                case REMOVE_BY_ID:
                    RemoveById removeById = new RemoveById();
                    answer = removeById.removeById(arg.getArgB(), collection, user);
                    break;
                case ADD_IF_MAX:
                    String[] valuesMax = arg.getArgA().toArray(new String[9]);
                    AddCore addIfMax = new AddCore(collection, this, valuesMax, user);
                    try {
                        answer = addIfMax.add(arg.getArgB(), true);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                    break;
                case REMOVE_GREATER:
                    RemoveGreater removeGreater = new RemoveGreater(arg.getArgB(), arg.getArgA().get(0), collection);
                    answer = removeGreater.splitter(user);
                    break;
                case HISTORY:
                    answer = history.history();
                    break;
                case MAX_BY_REAL_HERO:
                    MaxByRealHero maxByRealHero = new MaxByRealHero();
                    answer = maxByRealHero.maxByRealHero(collection);
                    break;
                case FILTER:
                    Filter filter = new Filter();
                    answer = filter.filter(collection, arg.getArgB());
                    break;
                case PRINT_DESCENDING:
                    DescendingSort descendingSort = new DescendingSort();
                    answer = descendingSort.descendingSort(collection);
                    break;
                case SIGN_IN:
                    String inputPass = new DBConnection().getUserByLogin(arg.getArgA().get(0));
                    String password = arg.getArgA().get(1);
                    if (inputPass != null) {
                        password = new Hasher().hash(password);
                        if (password.equals(inputPass)) {
                            answer = "Entry successful.";
                        } else {
                            answer = "Entry denied! Password is wrong";
                        }
                    } else {
                        answer = "No such user detected. Register yourself firstly.";
                    }
                    break;
                case SIGN_UP:
                    answer = new DBConnection().register(arg.getArgA().get(0), arg.getArgA().get(1));
                    if (answer == null) {
                        answer = "This login was already taken. Try again";
                    }
                    break;
            }
            Server.LOG.info("Message for client: {}", answer);
            return answer;
        } else {
            return "Your password has changed during the work. Please, check it out and try again";
        }
    }
}
