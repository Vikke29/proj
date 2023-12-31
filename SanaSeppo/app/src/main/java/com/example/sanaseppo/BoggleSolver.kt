package com.example.sanaseppo

class BoggleSolver(val board: Array<CharArray>, val dictionary: Set<String>) {
    private val trie = Trie()
    private val foundWords = mutableSetOf<String>()
    private val visited = Array(board.size) { BooleanArray(board[0].size) }

    init {
        dictionary.forEach { trie.insert(it) }
    }
    fun getPoints(wordLength: Int): Int {
        return when (wordLength) {
            3 -> 1
            4 -> 3
            5 -> 5
            6 -> 7
            7 -> 10
            8 -> 14
            9 -> 19
            10 -> 25
            11 -> 31
            12 -> 38
            13 -> 46
            14 -> 56
            15 -> 68
            16 -> 82
            17 -> 108
            18 -> 138
            19 -> 165
            else -> if (wordLength >=20) {200} else {0}
        }
    }

    fun solve(): Set<String> {
        for (i in board.indices) {
            for (j in board[i].indices) {
                dfs(i, j, "")
            }
        }
        return foundWords
    }

    private fun dfs(i: Int, j: Int, prefix: String) {
        if (i !in board.indices || j !in board[i].indices || visited[i][j]) return
        val word = prefix + board[i][j]
        if (!trie.startsWith(word)) return
        if (dictionary.contains(word)) foundWords.add(word)
        visited[i][j] = true
        dfs(i + 1, j, word)
        dfs(i - 1, j, word)
        dfs(i, j + 1, word)
        dfs(i, j - 1, word)
        dfs(i + 1, j + 1, word)
        dfs(i - 1, j - 1, word)
        dfs(i + 1, j - 1, word)
        dfs(i - 1, j + 1, word)
        visited[i][j] = false
    }
}

class Trie {
    private val root = TrieNode()

    fun insert(word: String) {
        var current = root
        for (c in word) {
            current = current.children.computeIfAbsent(c) { TrieNode() }
        }
        current.isWord = true
    }

    fun startsWith(prefix: String): Boolean {
        var current = root
        for (c in prefix) {
            current = current.children[c] ?: return false
        }
        return true
    }
}

class TrieNode(val children: MutableMap<Char, TrieNode> = mutableMapOf(), var isWord: Boolean = false)
