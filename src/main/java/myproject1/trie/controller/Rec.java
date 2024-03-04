package myproject1.trie.controller;

import myproject1.trie.trie.SimplePatternMatching;
import org.springframework.core.io.ClassPathResource;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.LinkedList;
import java.util.List;

public class Rec {
    public static void main(String[] args) {
        String recommendationCsvPath = "csv/Word_recommend.csv";
        List<String> recommendations = new LinkedList<>(); // 추천 단어를 저장할 리스트
        try (InputStream inputStream = new ClassPathResource(recommendationCsvPath).getInputStream();
             BufferedReader br = new BufferedReader(new InputStreamReader(inputStream, StandardCharsets.UTF_8))) {

            String line;
            while ((line = br.readLine()) != null) {
                // CSV 파일에서 읽은 데이터에서 쉼표 제거 후 리스트에 추가
                String recommendation = line.replace(",", "");
                recommendations.add(recommendation);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}


//SimplePatternMatching a = new SimplePatternMatching(); //단순 문자열 비교
//beforeTime = System.currentTimeMillis();
//List<String> result2 = a.searchPatterns(content,words);
//        System.out.println("result2 = " + result2);
//afterTime = System.currentTimeMillis();
//secDiffTime = (afterTime - beforeTime);
//        System.out.println("시간차이 : "+secDiffTime); 단순 문자열 비교 : 추후 시간초 확인할때 다시 넣기, 현재는 밖으로 빼버려서 빼둠