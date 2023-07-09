package com.example.phanmemqldt;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.*;
import javafx.scene.input.*;
import javafx.stage.*;


import java.io.IOException;
import java.sql.*;

public class HelloApplication extends Application {
    private double x,y,x1,y1;

    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("login-form.fxml"));
        Parent root = fxmlLoader.load();
        Scene scene = new Scene(root);
        root.setOnMousePressed((MouseEvent event)-> {
            x = event.getSceneX();
            y = event.getSceneY();
            x1 = event.getScreenX();
            y1 = event.getScreenY();
            System.out.println(x + " " + y + " " + x1 + " " + y1);
        });

        root.setOnMouseDragged((MouseEvent event)-> {
            stage.setX(event.getScreenX() - x);
            stage.setY(event.getScreenY() - y);
        });
        Connection connect = database.connectDb();
        if(connect != null){
            try
            {
                Statement stmt = connect.createStatement();
                ResultSet rs = stmt.executeQuery("select username, password from teachers");
                while (rs.next())
                    System.out.println(rs.getString(1) + "  " + rs.getString(2));
                connect.close();
            }
            catch (Exception e)
            {
                e.printStackTrace();
            }
        }
//
//        else
//            System.out.println("fail to connect database");
        stage.initStyle(StageStyle.TRANSPARENT);
        stage.setTitle("Hệ thống quản lý trường thcs");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}