import java.util.*;

public class aStar {
    static int nodeCount;
    public static class WordNode {
        String word;
        WordNode parent;
        int gScore;
        int hScore; 

        WordNode(String word, WordNode parent, int gScore, int hScore) {
            this.word = word;
            this.parent = parent;
            this.gScore = gScore;
            this.hScore = hScore;
        }

        int getFScore() {
            return gScore + hScore;
        }
    }

    public static boolean isClose(String word1, String word2) {
        if (word1.length() != word2.length())
        {
            return false;
        }
        int beda = 0;
        for (int i = 0; i < word1.length(); i++) 
        {
            if (word1.charAt(i) != word2.charAt(i)) 
            {
                if (++beda > 1)
                {
                    return false;
                }
            }
        }
        return beda == 1;
    }

    public static int calculateHScore(String word, String target) {
        int hScore = 0;
        for (int i = 0; i < word.length(); i++) {
            if (word.charAt(i) != target.charAt(i)) {
                hScore++;
            }
        }
        return hScore;
    }

    public static List<String> AStar(String startWord, String endWord, Set<String> words) {
        PriorityQueue<WordNode> openSet = new PriorityQueue<>(Comparator.comparingInt(node -> node.getFScore()));
        Map<String, Integer> gScores = new HashMap<>();
        openSet.offer(new WordNode(startWord, null, 0, calculateHScore(startWord, endWord)));
        gScores.put(startWord, 0);
        nodeCount = 0;

        while (!openSet.isEmpty()) {
            WordNode current = openSet.poll();
            String currentWord = current.word;

            if (currentWord.equals(endWord)) {
                List<String> ladder = new ArrayList<>();
                while (current != null) {
                    ladder.add(current.word);
                    current = current.parent;
                }
                Collections.reverse(ladder);
                return ladder;
            }

            for (String word : words) {
                if (isClose(currentWord, word)) {
                    nodeCount++;
                    int currentGScore = gScores.getOrDefault(currentWord, Integer.MAX_VALUE) + 1;
                    if (currentGScore < gScores.getOrDefault(word, Integer.MAX_VALUE)) {
                        WordNode newNode = new WordNode(word, current, currentGScore, calculateHScore(word, endWord));
                        openSet.offer(newNode);
                        gScores.put(word, currentGScore);
                    }
                }
            }
        }

        return null;
    }

    public static int getNodeCount() {
        return nodeCount;
    }
}
