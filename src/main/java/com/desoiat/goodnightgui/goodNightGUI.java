package com.desoiat.goodnightgui;

import java.text.SimpleDateFormat;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Button;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.text.Font;
import javafx.scene.text.FontPosture;
import javafx.scene.text.FontWeight;
import java.io.*;
import javax.sound.sampled.*;
import java.awt.*;
import java.util.Date;



public class goodNightGUI extends Application {

    static StackPane root = new StackPane();
    private String filename;
    private String path;
    private String songName;
    int breakTime = 15000;
    final int[] A = {0};
    int count = 0;
    int playTime;
    int totalTime;


    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws LineUnavailableException, IOException, UnsupportedAudioFileException {

        //标题
        primaryStage.setTitle("Good Night GUI");

        // current time
        SimpleDateFormat formatter= new SimpleDateFormat("yyyy-MM-dd 'at' HH:mm:ss");
        Date date = new Date(System.currentTimeMillis());


        //按钮1 实现 选择文件功能
        Button b1 = new Button();
        b1.setText("选择音频文件");
        b1.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                FileDialog fd = new FileDialog(new Frame(), "Choose a wav", FileDialog.LOAD);
                fd.setDirectory("C:\\");
                fd.setFile("*.wav");
                fd.setVisible(true);
                filename = fd.getFile();
                path = fd.getDirectory() + fd.getFile();
                songName = filename;
                b1.setText("当前音频 ：" + songName);
                System.out.println("音频文件为："+ songName);
            }
        });

        //获取播放次数
        javafx.scene.control.TextField tf = new javafx.scene.control.TextField();
        tf.setPrefWidth(50);
        tf.setMaxWidth(50);
        tf.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                count = Integer.parseInt(tf.getText());
                System.out.println("歌曲播放次数设置为： " + count + " 次 \n");
            }
        });
        Label label1 = new Label("歌曲播放次数 = ");
        Font font = Font.font("Brush Script MT", FontWeight.BOLD, FontPosture.REGULAR, 14);
        label1.setFont(font);
        label1.setTextFill(Color.BLACK);
        //Setting the position
        label1.setTranslateX(-280);
        label1.setTranslateY(-60);

        //LOG
        javafx.scene.control.TextArea textArea1 = new javafx.scene.control.TextArea();
        textArea1.setEditable(false);
        textArea1.setMaxWidth(240);
        textArea1.setMaxHeight(300);
        textArea1.setWrapText(true);

        System.setOut(new PrintStream(System.out) {
            @Override
            public void write(byte[] buf, int off, int len) {
                super.write(buf, off, len);
                String msg = new String(buf, off, len);
                textArea1.setText(textArea1.getText() + msg);
            }
        });


        //set
        Button SET = new Button();
        SET.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                try {
                    File yourFile;
                    AudioInputStream stream;
                    AudioFormat format;
                    DataLine.Info info;
                    Clip clip;

                    stream = AudioSystem.getAudioInputStream(new File(path));
                    format = stream.getFormat();
                    info = new DataLine.Info(Clip.class, format);
                    clip = (Clip) AudioSystem.getLine(info);
                    clip.open(stream);
                    clip.start();
                }
                catch (Exception e) {

                }
                A[0]++;
                Date date = new Date(System.currentTimeMillis());
                System.out.println("第"+A[0]+"次 "+"开始时间"+formatter.format(date));
            }
        });


        //调取 音频时间
        if(filename != null) {
            File fileTime = new File(path);
            Clip clip = AudioSystem.getClip();
            AudioInputStream ais = AudioSystem.getAudioInputStream(fileTime);
            clip.open(ais);
            long clipTime = clip.getMicrosecondLength();
            playTime = (int) ((clipTime / 1000000) * 1000);
        }


        //调整间隔时间
        javafx.scene.control.TextField tf1 = new javafx.scene.control.TextField();
        tf1.setPrefWidth(50);
        tf1.setMaxWidth(50);
        tf1.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                breakTime = (Integer.parseInt(tf1.getText())) * 1000;
                totalTime = playTime + breakTime;
                System.out.println("歌曲播放间隔时间设置为： " + tf1.getText() + " 秒 \n");

            }
        });
        Label label = new Label("歌曲播放间隔 = ");
        label.setFont(font);
        label.setTextFill(Color.BLACK);
        //Setting the position
        label.setTranslateX(-280);
        label.setTranslateY(-120);

        //start
        Button play = new Button();
        play.setText("循环开始");
        play.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                try {

                        System.out.println("循环开始");
                        int AimTime = totalTime * count;
                        convertMillis(AimTime);

                    if(songName != null) {
                        for (int i = 0; i < count; i++) {
                            SET.fire();
                            Thread.sleep(totalTime);
                        }
                    }
                    else
                        System.out.println("未选择音频，无法播放");

                }
                catch (Exception e) {

                }

            }
        });






        //添加 按钮 与 文字

        root.getChildren().add(b1);
        root.getChildren().add(play);
        root.getChildren().add(tf);
        root.getChildren().add(tf1);
        root.getChildren().add(label);
        root.getChildren().add(label1);
        root.getChildren().add(textArea1);




        //调整 按钮 CSS
        String guiCss = "https://raw.githubusercontent.com/DeSoiat/GoodNightGUI/main/src/main/resources/com/desoiat/goodnightgui/css/gui.css";

        b1.setPrefSize(180,30);
        b1.setTranslateX(-250); //因为 使用了 stack 所以必须使用 translatex 才可以移动
        b1.setTranslateY(-170);
        b1.getStylesheets().add(guiCss);

        play.setPrefSize(180,30);
        play.setTranslateY(-250);
        play.setTranslateY(150);
        play.getStylesheets().add(guiCss);

        tf.setTranslateX(-200);
        tf.setTranslateY(-60);

        tf1.setTranslateX(-200);
        tf1.setTranslateY(-120);

        root.setId("stack-pane");
        root.getStylesheets().add(guiCss);

        textArea1.setTranslateX(180);
        textArea1.setTranslateY(-15);
        textArea1.getStylesheets().add(guiCss);




        //应用窗口大小 与 窗口图标
        primaryStage.setScene(new Scene(root, 700, 400));
        primaryStage.setResizable(false);
        primaryStage.show();


        //MESSAGE

        System.out.println("目前程序只支持wav格式音频文件并且永远不会支持MP3格式\n");
        System.out.println("更改 播放间隔 与 次数 需要在输入数字后按下enter键\n");
        System.out.println("当前时间为 "+formatter.format(date)+"\n");


    }

    public static void convertMillis(int Millis){
        String convert = String.format("预计需要 %d 小时, %d 分, and %d 秒",

                Millis/(1000*60*60), (Millis%(1000*60*60))/(1000*60), ((Millis%(1000*60*60))%(1000*60))/1000);

        System.out.println(convert);
    }


}


