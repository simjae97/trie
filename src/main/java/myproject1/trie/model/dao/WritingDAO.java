package myproject1.trie.model.dao;


import myproject1.trie.model.dto.WritingDTO;
import org.springframework.stereotype.Component;

@Component
public class WritingDAO extends DAO{
    public boolean write(WritingDTO writingDTO){
        try {
            String sql= "insert into writing(id,pw,content,title) values(?,?,?,?)";
            ps = conn.prepareCall(sql);
            ps.setString(1,writingDTO.getId());
            ps.setString(2,writingDTO.getPw());
            ps.setString(3,writingDTO.getContent());
            ps.setString(4,writingDTO.getTitle());
            int count = ps.executeUpdate();
            if(count == 1){
                return true;
            }
        }
        catch (Exception e){
            System.out.println("e = " + e);
        }

        return false;
    }
}
