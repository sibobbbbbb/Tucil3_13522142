import java.util.*;

public class gbfs {
    static int nodeCount;

    public static class WordNode {
        String word;
        int heuristic;

        WordNode(String word, int heuristic) {
            this.word = word;
            this.heuristic = heuristic;
        }
    }

    public static List<String> reconstructPath(Map<String, String> parentMap, String endWord) {
        List<String> path = new ArrayList<>();
        String currentWord = endWord;
        while (currentWord != null) {
            path.add(currentWord);
            currentWord = parentMap.get(currentWord);
        }
        Collections.reverse(path);
        return path;
    }

    private static List<String> generateNeighbors(String word, Set<String> wordList) {
        List<String> neighbors = new ArrayList<>();
        for (int i = 0; i < word.length(); i++) {
            char[] charArray = word.toCharArray();
            for (char c = 'a'; c <= 'z'; c++) {
                charArray[i] = c;
                String newWord = String.valueOf(charArray);
                if (!newWord.equals(word) && wordList.contains(newWord)) {
                    neighbors.add(newWord);
                }
            }
        }
        return neighbors;
    }

    public static int calculateHeuristic(String word, String target) {
        int heuristic = 0;
        for (int i = 0; i < word.length(); i++) {
            if (word.charAt(i) != target.charAt(i)) {
                heuristic++;
            }
        }
        return heuristic;
    }

    public static List<String> GBFS(String startWord, String endWord, Set<String> words) {
        nodeCount = 0;
        PriorityQueue<WordNode> openSet = new PriorityQueue<>(Comparator.comparingInt(WordNode -> WordNode.heuristic));
        Set<String> closedSet = new HashSet<>();
        Map<String, String> parentMap = new HashMap<>();
        openSet.offer(new WordNode(startWord, calculateHeuristic(startWord, endWord)));

        while (!openSet.isEmpty()) {
            String currentWord = openSet.poll().word;
            closedSet.add(currentWord);

            if (currentWord.equals(endWord)) {
                return reconstructPath(parentMap, endWord);
            }

            List<String> neighbors = generateNeighbors(currentWord, words);
            for (String neighbor : neighbors) {
                if (!closedSet.contains(neighbor)) {
                    nodeCount++;
                    openSet.offer(new WordNode(neighbor, calculateHeuristic(neighbor, endWord)));
                    parentMap.put(neighbor, currentWord);
                }
            }
        }

        return null;
    }

    public static int getNodeCount() {
        return nodeCount;
    }
}
