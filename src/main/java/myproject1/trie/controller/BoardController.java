package myproject1.trie.controller;


import lombok.extern.slf4j.Slf4j;
import myproject1.trie.model.dao.WritingDAO;
import myproject1.trie.model.dto.WritingDTO;
import myproject1.trie.trie.AhoCorasick;
import myproject1.trie.trie.SimplePatternMatching;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.io.*;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.*;

@Slf4j
@Controller
public class BoardController {

    @Autowired
    WritingDAO writingDAO;
    AhoCorasick filter;

    AhoCorasick rec;

    public BoardController() {//욕설필터링에 사용할 ahokora식은 변경 될 일이 없으므로 생성자에 한번만 생성
        //csv받아와서 Trie에 저장,현재는 단순 비교이므로 aho corasick으로 변경 준비
        // 반환용 리스트 변수
        // 입력 스트림 오브젝트 생성
        // CSV 파일 경로
        List<String> words = new LinkedList<>();
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
        this.filter = new AhoCorasick(words);
        logupdate();
    }

    //자동완성용 문자추천 csv의 경우 csv가 누적되며 바뀌므로 이벤트 스케쥴러처럼 주기마다 갱신되게 하기?
    @Scheduled(cron = "0 0 0/1 * * *") //1시간에 한번 실행되게 하기
    public void logupdate(){
        List<String>words2 = new LinkedList<>();
        BufferedReader br = null;
        try {
            // 대상 CSV 파일의 경로 설정
            br = Files.newBufferedReader(Paths.get("build/resources/main/static/csv/Word_recommend.csv"), Charset.forName("UTF-8")); //csv 위치 : 서버켜진상태에서 계속 갱신되므로 build에서 가져오기
            // CSV파일에서 읽어들인 1행분의 데이터
            String line = "";

            while((line = br.readLine()) != null) {
                // CSV 파일의 1행을 저장하는 리스트 변
                // 반환용 리스트에 1행의 데이터를 저장
                String answer =line.replace("," , "");
                words2.add(answer);
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try{
                if(br != null) {
                    br.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }

        }
        List<String>words3 = new LinkedList<>(words2); //중복 제거후 리스트로 전달 -> 단순 순서대로 추천하기로 했기때문, 빈도수로 할거면 재설계
        this.rec = new AhoCorasick(words3);
    }

    public void appendToCsv(List<String> inputStrings, Resource outputPath) throws IOException {
        // 파일 경로를 얻기 위해 Resource를 File로 변환
        Path path = outputPath.getFile().toPath();
        if (!Files.exists(path.getParent())) {
            Files.createDirectories(path.getParent());
        }
        // 파일에 데이터를 추가하기 위해 BufferedWriter 생성
        try (BufferedWriter writer = Files.newBufferedWriter(path, StandardCharsets.UTF_8,StandardOpenOption.APPEND)) {
            // 입력된 문자열 리스트를 CSV 파일에 추가함
            for (String inputString : inputStrings) {
                // 각 문자열을 쉼표로 구분하여 파일에 추가함
                writer.write(inputString);
                writer.write(","); // 쉼표 추가
                writer.newLine(); // 다음 줄로 이동
            }
        }
    }
    

    @PostMapping("/write")
    @ResponseBody
    public boolean write(WritingDTO writingDTO){
        System.out.println("writingDTO = " + writingDTO);
        boolean result =  writingDAO.write(writingDTO); // 추후 등록한 다음 로그파일로 빼게 구현
        Resource resource = new ClassPathResource("static/csv/Word_recommend.csv");
        List<String> inputStrings = List.of(writingDTO.getContent().split(" "));
        try {
            // CSV 파일에 입력 데이터를 추가함
            appendToCsv(inputStrings, resource);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return result;
    }

    @GetMapping("/")
    public String view(){

        return "hi";
    }

    @PostMapping("/check")
    @ResponseBody
    public List<String> csv1( String content){
        System.out.println("text = " + content);
        // Aho-Corasick 알고리즘을 사용하여 단어 검색

        // Word_recommend.csv 파일 처리

        long beforeTime = System.currentTimeMillis();
        long afterTime = System.currentTimeMillis(); // 코드 실행 후에 시간 받아오기
        long secDiffTime = (afterTime - beforeTime); //두 시간에 차 계산

         //아호코라식에 넣기위해 만들어둔 리스트를 넣음
        beforeTime = System.currentTimeMillis();
        List<String> result = filter.search(content);
        System.out.println("result = " + result);
        afterTime = System.currentTimeMillis();
        secDiffTime = (afterTime - beforeTime);
        System.out.println("시간차이 : "+secDiffTime);


        return result;

    }

    @PostMapping("/recommend")
    @ResponseBody
    public List<String> recommend( String content){

        Map<String ,Integer> result1 = rec.autocomplete(content);

        List<Map.Entry<String, Integer>> sortedList = new ArrayList<>(result1.entrySet());
        sortedList.sort(Map.Entry.comparingByValue(Comparator.reverseOrder()));

        // 정렬된 리스트를 이용하여 ArrayList 생성
        List<String> result = new ArrayList<>();
        for (Map.Entry<String, Integer> entry : sortedList) {
            System.out.println("키"+entry.getKey());
            System.out.println("밸류"+entry.getValue());
            result.add(entry.getKey());
        }
        return result;

    }
}
