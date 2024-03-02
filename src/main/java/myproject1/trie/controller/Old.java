package myproject1.trie.controller;

import myproject1.trie.trie.AhoCorasick;
import myproject1.trie.trie.SimplePatternMatching;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.LinkedList;
import java.util.List;

public class Old {
    public static void main(String[] args) {
        //csv받아와서 Trie에 저장,현재는 단순 비교이므로 aho corasick으로 변경 준비
        // 반환용 리스트 변수
        // 입력 스트림 오브젝트 생성
        BufferedReader br = null;
        List<String> list = new LinkedList<>(); //트라이구조에 넣을 문자열 배열 생성
        String test = "";
        try {
            // 대상 CSV 파일의 경로 설정
            br = Files.newBufferedReader(Paths.get("C:\\Users\\504\\Desktop\\trie\\src\\main\\java\\myproject1\\trie\\csv\\Word_Filter.csv"), Charset.forName("UTF-8")); //csv 위치
            // CSV파일에서 읽어들인 1행분의 데이터
            String line = "";

            while((line = br.readLine()) != null) {
                // CSV 파일의 1행을 저장하는 리스트 변
                // 반환용 리스트에 1행의 데이터를 저장
                String answer =line.replace("," , "");
                list.add(answer); //내 구현용 리스트 대입
                test += answer; //단순 비교용 문자열 만들기
                test += "안녕"; //단순 비교용 문자열 만들기
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
        List<String> list2 = new LinkedList<>(); //트라이구조에 넣을 문자열 배열 생성
        String test2 = "";

        try {
            // 대상 CSV 파일의 경로 설정
            br = Files.newBufferedReader(Paths.get("myproject1/trie/csv/Word_recommend.csv"),Charset.forName("UTF-8")); //csv 위치
            // CSV파일에서 읽어들인 1행분의 데이터
            String line = "";

            while((line = br.readLine()) != null) {
                // CSV 파일의 1행을 저장하는 리스트 변
                // 반환용 리스트에 1행의 데이터를 저장
                String answer =line.replace("," , "");
                list2.add(answer); //내 구현용 리스트 대입

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
        long beforeTime = System.currentTimeMillis();
        long afterTime = System.currentTimeMillis(); // 코드 실행 후에 시간 받아오기
        long secDiffTime = (afterTime - beforeTime); //두 시간에 차 계산

        AhoCorasick ret = new AhoCorasick(list); //아호코라식에 넣기위해 만들어둔 리스트를 넣음
        beforeTime = System.currentTimeMillis();
        List<String> result = ret.search(test);
        System.out.println("result = " + result);
        afterTime = System.currentTimeMillis();
        secDiffTime = (afterTime - beforeTime);
        System.out.println("시간차이 : "+secDiffTime);

        SimplePatternMatching a = new SimplePatternMatching(); //단순 문자열 비교
        beforeTime = System.currentTimeMillis();
        List<String> result2 = a.searchPatterns(test,list);
        System.out.println("result2 = " + result2);
        afterTime = System.currentTimeMillis();
        secDiffTime = (afterTime - beforeTime);
        System.out.println("시간차이 : "+secDiffTime);



        String prefix = "안"; //어떻게 로그처리해서 추천을 넣을지, 추천의 우선순위(가나다순인지,빈도순으로 할지) 고민
        AhoCorasick ret2 = new AhoCorasick(list2);
        List<String> suggestions = ret2.autocomplete(prefix);
        System.out.println("검색어 '" + prefix + "'를 위한 자동완성 제안:");
        for (String suggestion : suggestions) {
            System.out.println(suggestion);
        }
    }
}
