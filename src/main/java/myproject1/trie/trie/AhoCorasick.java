package myproject1.trie.trie;



import java.util.*;

public class AhoCorasick {
    private TrieNode root; //맨처음노드

    public AhoCorasick(List<String> patterns) {
        root = new TrieNode(); //부모노드 일단 만들고
        buildTrie(patterns); //생성자 대신 빌드로 만들어둔 패턴리스트 대입
        buildFailTransitions();
    }


    private void buildTrie(List<String> patterns) { //받아온 리스트 패턴을 그대로 대입해줘서 Trie구조 생성
        for (String pattern : patterns) {
            TrieNode node = root;
            for (char c : pattern.toCharArray()) {
                node.children.putIfAbsent(c, new TrieNode()); // c라는 key값이 존재하지 않을때에만 넣어주겠다
                node = node.children.get(c); //현재 노드를 자식노드로 변경
            }
            node.output.add(pattern); //종료되는 패턴 저장
            node.isEndOfWord = true;
        }
    }

    private void buildFailTransitions() {
        Queue<TrieNode> queue = new LinkedList<>(); //실패상태는 queue구조 , KMP와 유사
        for (TrieNode child : root.children.values()) {
            queue.add(child);
            child.fail = root; //초기화시 실패할때 루트로 돌아간다
        }
        while (!queue.isEmpty()) {
            TrieNode node = queue.poll(); //넣은 순서대로 다시 큐에서 뽑아옴
            for (Map.Entry<Character, TrieNode> entry : node.children.entrySet()) {
                char c = entry.getKey();
                TrieNode child = entry.getValue();
                queue.add(child);
                TrieNode failState = node.fail;
                while (failState != null && !failState.children.containsKey(c)) { //failState 현재 노드의 실패 노드를 나타내며, failState를 이용하여 해당 노드의 실패 노드를 찾는 과정
                                                                                //현재는 모든 코드가 root여서 단순히 root노드로 돌아가나? 제대로 구현됐는지 헷갈림
                    failState = failState.fail;
                }
                child.fail = failState != null ? failState.children.get(c) : root; //아마 여기에서 페일노드 바꿔 주는거맞는거같은데 코드리뷰하다 헷갈린듯함.
                child.output.addAll(child.fail.output);
            }
        }
    }

    public List<String> search(String text) {
        List<String> results = new ArrayList<>(); //결과값 받아와줄 리설트 생성
        TrieNode currentState = root;   //현재 위치 = 맨처음은 부모
        for (int i = 0; i < text.length(); i++) {   //받아온 문자열반복문 돌리기
            char c = text.charAt(i);    //trie구조이므로 char형태로
            while (currentState != null && !currentState.children.containsKey(c)) {
                currentState = currentState.fail;
            }
            if (currentState == null) {
                currentState = root;
                continue;
            }
            currentState = currentState.children.get(c);
            results.addAll(currentState.output);
        }
        return results;
    }
    public List<String> autocomplete(String prefix) { //원래 트라이구조 사용처인 검색어 기능을 추가해 자동완성 기능 추가
        List<String> suggestions = new ArrayList<>(); //리턴할 문자열 리스트 생성
        TrieNode node = root; //부모 루트부터 시작
        for (char c : prefix.toCharArray()) { //문자열 향상된 포문
            if (!node.children.containsKey(c)) {
                return suggestions; // 문자열이 없을경우
            }
            node = node.children.get(c); //다음노드로 이동
        }
        getAllWords(node, prefix, suggestions); //저 문자열에 해당하는 노드위치를 알았으니 이제 리스트에 그거보다 아래인 자식문자열 찾기
        return suggestions;
    }


    private void getAllWords(TrieNode node, String prefix, List<String> suggestions) {
        if (node.isEndOfWord) {
            suggestions.add(prefix); //끝문자면 바로 대입
        }
        for (char c : node.children.keySet()) { //직계 자손에 대한 반복문이므로 단순히 반복문을 돌려도 문제가 없음
            getAllWords(node.children.get(c), prefix + c, suggestions); //재귀형태로 구현
        }
    }
}