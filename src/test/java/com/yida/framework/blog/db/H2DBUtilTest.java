package com.yida.framework.blog.db;

import com.yida.framework.blog.utils.h2.H2DBUtil;

import java.sql.SQLException;

/**
 * @Author Lanxiaowei
 * @Date 2018-06-21 17:53
 * @Description H2DBUtil工具类测试
 */
public class H2DBUtilTest {
    public static void main(String[] args) throws SQLException {
        //删除表测试
        dropTable();
        //创建表测试
        createTable();

        System.out.println(Boolean.valueOf("1"));

        /*//插入数据测试
        insertData();
        //查询表测试
        queryTable();
        //删除表数据测试
        clearData();*/
    }

    public static void dropTable() throws SQLException {
        String dropSQL = "DROP TABLE cookies";
        H2DBUtil.executeUpdate(dropSQL, null);
    }

    public static void createTable() throws SQLException {
        String createSQL = "CREATE TABLE cookies(id bigint auto_increment primary key, "
                + "                                site_id varchar(20), "
                + "                                cookie_key varchar(100), "
                + "                                cookie_val varchar(2000))";

        H2DBUtil.executeUpdate(createSQL, null);
    }

    public static void queryTable() throws SQLException {
        String sql = "select * from cookies";
        H2DBUtil.executeQuery(sql, null);
    }

    public static void insertData() throws SQLException {
        String sql = "insert into cookies(site_id,cookie_key,cookie_val) values(?,?,?)";
        H2DBUtil.executeUpdate(sql, new Object[]{"github", "_ahj", "uuid_tt_dd=1233261396770835785_20171126; csdn_tt_dd=v10_cc91e6490f381a75aa9d4004b2ea9c2fc993e7361101dc5dbeb5c37fedd29284; UE=\\\"vnetoolxw_87@163.com\\\"; kd_user_id=ab7d70ee-bd0e-41b5-808c-9e9b7e1c6462; sensorsdata2015jssdkcross=%7B%22distinct_id%22%3A%2216026782080d1-09479f8b739aac-464c0328-2073600-160267820816ff%22%2C%22%24device_id%22%3A%2216026782080d1-09479f8b739aac-464c0328-2073600-160267820816ff%22%7D; gr_user_id=f3a8d719-fe44-4d34-ae3f-61117a74eee4; _ga=GA1.2.1645377725.1516633775; Hm_ct_6bcd52f51e9b3dce32bec4a3997715ac=1788*1*PC_VC; UN=vnetoolxw_87; dc_session_id=10_1529207119260.739075; JSESSIONID=5E69164CC2C5A5B540158521C7E9F040.tomcat2; smidV2=201806191754495a809c78fbe2fa2145c1ae6ab365049000afdccb6730cd560; BT=1529413854993; LSSC=LSSC-390603-exFu2BvpyuFeLc0sM1LlzCB7EK5DMY-passport.csdn.net; Hm_lvt_6bcd52f51e9b3dce32bec4a3997715ac=1529413734,1529414216,1529414236,1529414846; Hm_lpvt_6bcd52f51e9b3dce32bec4a3997715ac=1529415080; dc_tos=pako87"});
    }

    public static void clearData() throws SQLException {
        String sql = "delete from cookies";
        H2DBUtil.executeUpdate(sql, null);
    }
}
