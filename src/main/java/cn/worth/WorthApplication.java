package cn.worth;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

//import org.activiti.spring.boot.SecurityAutoConfiguration;

/**
 * @Author: chenxiaoqing9
 * @Date: Created in 2019/3/6
 * @Description:
 * @Modified by:
 */
@SpringBootApplication//(exclude = SecurityAutoConfiguration.class)//activiti启动报错解决方式 exclude = SecurityAutoConfiguration.class
//@EnableScheduling
@MapperScan({"cn.worth.**.mapper"})
//@EnableSwagger2Doc
public class WorthApplication {

    public static void main(String[] args) {
        SpringApplication.run(WorthApplication.class, args);

        printHaveFun();
    }


    private static void printHaveFun() {
        System.out.println(" ___  ___  ________  ___      ___ _______           ________ ___  ___  ________      \n" +
                "|\\  \\|\\  \\|\\   __  \\|\\  \\    /  /|\\  ___ \\         |\\  _____\\\\  \\|\\  \\|\\   ___  \\    \n" +
                "\\ \\  \\\\\\  \\ \\  \\|\\  \\ \\  \\  /  / | \\   __/|        \\ \\  \\__/\\ \\  \\\\\\  \\ \\  \\\\ \\  \\   \n" +
                " \\ \\   __  \\ \\   __  \\ \\  \\/  / / \\ \\  \\_|/__       \\ \\   __\\\\ \\  \\\\\\  \\ \\  \\\\ \\  \\  \n" +
                "  \\ \\  \\ \\  \\ \\  \\ \\  \\ \\    / /   \\ \\  \\_|\\ \\       \\ \\  \\_| \\ \\  \\\\\\  \\ \\  \\\\ \\  \\ \n" +
                "   \\ \\__\\ \\__\\ \\__\\ \\__\\ \\__/ /     \\ \\_______\\       \\ \\__\\   \\ \\_______\\ \\__\\\\ \\__\\\n" +
                "    \\|__|\\|__|\\|__|\\|__|\\|__|/       \\|_______|        \\|__|    \\|_______|\\|__| \\|__|\n" +
                "                                                                                     \n" +
                "                                                                                     ");
    }
}
