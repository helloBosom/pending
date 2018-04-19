package util;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * @author Legend-novo
 */
public class HttpTest extends Application {
    private String message = null;

    /**
     * 将对象转换成JSON对象数据
     *
     * @return 返回JSON对象数据
     */
    public String setJson() {
        Person person1 = new Person("zhangsan", 21);
        Person person2 = new Person("lisi", 25);
        Person person3 = new Person("tom", 31);
        final List<Person> list = new ArrayList<>();
        list.add(person1);
        list.add(person2);
        list.add(person3);
        return JsonStr.getJson(list);
    }

    /**
     * 以GET的方式进行数据交互
     *
     * @param primaryStage
     */
    public void GETHttp(Stage primaryStage) {

        final HashMap<String, String> map = new HashMap<>();
        map.put("data", setJson());
        System.out.println(setJson());

        final Label labelget = new Label();
        final Label labelsend = new Label();
        Button btnget = new Button();
        btnget.setText("获取信息");
        Button btnsend = new Button();
        btnsend.setText("发送信息");
        btnget.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                System.out.println("点击了btnget！");
                try {
                    message = HttpMethod.getGETString();
                    labelget.setText(message);
                } catch (Exception ex) {
                    Logger.getLogger(HttpTest.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        });

        btnsend.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                System.out.println("点击了btnsend！");
                try {
                    if (HttpMethod.sendGETString(map, "UTF-8")) {
                        labelsend.setText("sending successed");
                    } else {
                        labelsend.setText("sending failed");
                    }
                } catch (Exception ex) {
                    Logger.getLogger(HttpTest.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        });
        VBox vb = new VBox(10);
        vb.getChildren().addAll(btnget, labelget, btnsend, labelsend);
        StackPane root = new StackPane();
        root.getChildren().add(vb);

        Scene scene = new Scene(root, 300, 250);

        primaryStage.setTitle("GET方式数据交互");
        primaryStage.setScene(scene);
    }

    public void POSTHttp(Stage primaryStage) {

        final HashMap<String, String> map = new HashMap<>();
        map.put("data", setJson());
        System.out.println(setJson());

        final Label labelget = new Label();
        final Label labelsend = new Label();
        Button btnget = new Button();
        btnget.setText("获取信息");
        Button btnsend = new Button();
        btnsend.setText("发送信息");
        btnget.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                System.out.println("点击了btnget！");
                try {
                    message = HttpMethod.getPOSTString();
                    labelget.setText(message);
                } catch (Exception ex) {
                    Logger.getLogger(HttpTest.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        });

        btnsend.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                System.out.println("点击了btnsend！");
                try {
                    if (HttpMethod.sendGETString(map, "UTF-8")) {
                        labelsend.setText("sending successed");
                    } else {
                        labelsend.setText("sending failed");
                    }
                } catch (Exception ex) {
                    Logger.getLogger(HttpTest.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        });
        VBox vb = new VBox(10);
        vb.getChildren().addAll(btnget, labelget, btnsend, labelsend);
        StackPane root = new StackPane();
        root.getChildren().add(vb);

        Scene scene = new Scene(root, 300, 250);

        primaryStage.setTitle("POST方式数据交互");
        primaryStage.setScene(scene);
    }

    @Override
    public void start(Stage primaryStage) {
//        POSTHttp(primaryStage);
        GETHttp(primaryStage);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}