import java.util.*;

public class ucs {
    static int nodeCount;
    public static class WordNode {
        String word;
        WordNode parent;
        int cost;

        WordNode(String word, WordNode parent, int cost) {
            this.word = word;
            this.parent = parent;
            this.cost = cost;
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

    public static List<String> UCS(String startWord, String endWord, Set<String> words) {
        Queue<WordNode> queue = new PriorityQueue<>(Comparator.comparingInt(node -> node.cost));
        Set<String> visited = new HashSet<>();
        queue.offer(new WordNode(startWord, null, 0));
        visited.add(startWord);
        nodeCount = 0;

        while (!queue.isEmpty()) {
            WordNode current = queue.poll();
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
                if (!visited.contains(word) && isClose(currentWord, word)) {
                    nodeCount++;
                    queue.offer(new WordNode(word, current, current.cost + 1));
                    visited.add(word);
                }
            }
        }

        return null;
    }

    public static int getNodeCount() {
        return nodeCount;
    }
}
