package com.logic.transfer;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.URLEncoder;

public class DownloadServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        doPost(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String file = req.getParameter("");
        //告诉浏览器是以下载的方式获取资源
        resp.setHeader("content-disposition", "attachment;filename =" + URLEncoder.encode(file, "utf-8"));
        //监听 接受文件
        new DownloadReceive().start(8612, resp);
    }
}
