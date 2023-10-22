package model;

public class Main {
    public static void main(String[] args) throws Exception {
//        Board board = new Board(10, 10, 5);
//        View view = new View();
//        Model model = new Model(board);
//
//
//        Controller controller = new Controller(model, view);
//        controller.gameLoop();

        UserFileManager.insertUser("daba", "12345", "test.list");
        UserFileManager.deleteUser("daba",  "test.list");
        System.out.println(UserFileManager.validateUser("daba", "1234", "test.list"));
        System.out.println(UserFileManager.validateUser("daba", "12345", "test.list"));
        System.out.println(UserFileManager.validateUser("dauba", "12345", "test.list"));
    }
}