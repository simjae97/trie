package myproject1.trie.controller;


import myproject1.trie.model.dao.WritingDAO;
import myproject1.trie.model.dto.WritingDTO;
import myproject1.trie.trie.AhoCorasick;
import myproject1.trie.trie.SimplePatternMatching;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.LinkedList;
import java.util.List;

@Controller
public class BoardController {

    @Autowired
    WritingDAO writingDAO;


    @GetMapping("/")
    public String view(){

        return "hi";
    }

    @PostMapping("/write")
    @ResponseBody
    public boolean write(WritingDTO writingDTO){
        System.out.println("writingDTO = " + writingDTO);
        return writingDAO.write(writingDTO); // 추후 등록한 다음 로그파일로 빼게 구현
    }

    @PostMapping("/check")
    @ResponseBody
    public List<String> csv1( String content){
        System.out.println("text = " + content);
        //csv받아와서 Trie에 저장,현재는 단순 비교이므로 aho corasick으로 변경 준비
        // 반환용 리스트 변수
        // 입력 스트림 오브젝트 생성
        List<String> words = new LinkedList<>(); // 단어를 저장할 리스트
        // CSV 파일 경로
        try {
            // CSV 파일 경로
            Resource resource = new ClassPathResource("static/csv/Word_Filter.csv");
            InputStream inputStream = resource.getInputStream();

            // CSV 파일에서 단어 읽어오기
            try (BufferedReader br = new BufferedReader(new InputStreamReader(inputStream, StandardCharsets.UTF_8))) {
                String line;
                while ((line = br.readLine()) != null) {
                    // CSV 파일에서 읽은 데이터에서 쉼표 제거 후 리스트에 추가
                    String word = line.replace(",", "");
                    words.add(word);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        // Aho-Corasick 알고리즘을 사용하여 단어 검색
        // 이 부분에서 Aho-Corasick 알고리즘을 적용하여 단어 검색 로직을 추가해야 합니다.

        // Word_recommend.csv 파일 처리

        long beforeTime = System.currentTimeMillis();
        long afterTime = System.currentTimeMillis(); // 코드 실행 후에 시간 받아오기
        long secDiffTime = (afterTime - beforeTime); //두 시간에 차 계산

        AhoCorasick ret = new AhoCorasick(words); //아호코라식에 넣기위해 만들어둔 리스트를 넣음
        beforeTime = System.currentTimeMillis();
        List<String> result = ret.search(content);
        System.out.println("result = " + result);
        afterTime = System.currentTimeMillis();
        secDiffTime = (afterTime - beforeTime);
        System.out.println("시간차이 : "+secDiffTime);

        SimplePatternMatching a = new SimplePatternMatching(); //단순 문자열 비교
        beforeTime = System.currentTimeMillis();
        List<String> result2 = a.searchPatterns(content,words);
        System.out.println("result2 = " + result2);
        afterTime = System.currentTimeMillis();
        secDiffTime = (afterTime - beforeTime);
        System.out.println("시간차이 : "+secDiffTime);

        return result;

    }
}
