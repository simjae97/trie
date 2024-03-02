package myproject1.trie.model.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class DAO {
    public Connection conn; // DB연동 결과 객체를 연결 , 기재된 SQL Statement객체 반환.
    public PreparedStatement ps;  // 기재된 SQL에 매개변수 할당 , SQL 실행
    public ResultSet rs;          // select 결과 여러개 레코드를 호출
    public DAO(){         // db연동를 생성자에서 처리
        try {
            // 1. mysql JDBC 호출 ( 각 회사별  상이 , 라이브러리 다운로드 )
            Class.forName("com.mysql.cj.jdbc.Driver");
            // 2. 해당 db서버의 주소와 db연동
            conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/springp1", "root", "1234");
        }catch (Exception e ){   System.out.println(e); }
    }
}
