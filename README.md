# Game of life

This app is a cellular automaton, modeling the surviving process of a cell under a set of rules determining the conditions of dying, surviving, and reproduction.

Here are these rules: (we suppose living cell is non-empty cell, and dead is empty one)
1. If a living cell has less than 2 living cells in adjacent 8 cells, then it dies by loneliness.
2. If a cell has 2 living neighbors, then the living cell keeps alive.
3. If a cell has 3 adjacent living cells then it becomes alive if it was dead or keeps alive if it was alive.
4. If a living cell has over 3 adjacent living cells, it dies by overpopulation.
