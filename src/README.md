# Assignment 2: Finding Repeated K-mers in DNA Sequences
**Student:** Ana Guruli  
**Language:** Java  

## 1. Introduction

Finding repeated substrings of fixed length K (“K-mers”) in DNA sequences is a fundamental task in bioinformatics, with applications in genome assembly, motif detection, and sequence alignment.

This project implements and compares three algorithmic approaches in Java:

1. **Brute Force** – baseline nested comparison
2. **HashMap-based counting** – efficient string hashing and lookup
3. **Sort-based approach** – lexicographical sorting of K-mer/position pairs

Benchmarks are performed across multiple sequence lengths (N = 10K, 50K, 100K), K values (10, 50, 200), and three DNA types:

- **Uniform Random DNA** – equal probability of A/C/G/T
- **Highly Repetitive DNA** – tandem repeats
- **GC-Biased DNA (70%)** – gene-rich regions

---

## 2. Algorithms and Complexity Analysis

### 2.1 Brute Force (Baseline)

- **Approach**: Compare every pair of K-mers character-by-character.
- **Time Complexity**: O((N-K)² × K)
- **Space Complexity**: O(R × K), where R = number of repeated K-mers stored
- **Limitations**: Becomes infeasible for N > 20K due to quadratic time and high memory usage.

### 2.2 HashMap-based Counting

- **Approach**: Slide a window over the DNA sequence, store each K-mer in a HashMap mapping to a list of positions, then filter K-mers appearing ≥2 times.
- **Time Complexity**: O(N × K)
    - Note: String hashing in Java requires O(K) per substring; true “O(1)” lookups are only average-case.
- **Space Complexity**: O(N × K) (storing each K-mer and positions)
- **Advantages**: Extremely fast for moderate K and N; handles large sequences efficiently.

### 2.3 Sort-based Approach

- **Approach**: Generate (K-mer, position) pairs, sort lexicographically, and scan adjacent pairs to detect duplicates.
- **Time Complexity**: O(N × log N × K) worst-case
    - In practice, DNA’s limited alphabet reduces comparison cost.
- **Space Complexity**: O(N × K)
- **Use Case**: Outperforms HashMap for extremely large K (>1000) or massive N (>10M) due to consistent memory access patterns.

---

## 3. Experimental Setup

- **Sequence lengths**: N = 10K, 50K, 100K
- **K values**: 10, 50, 200
- **DNA types**: Uniform Random, Highly Repetitive, GC-Biased (70%)
- **Benchmark protocol**:
    - 3 warm-up runs per algorithm
    - 5 measured runs per algorithm
    - Average execution time reported in milliseconds
    - Approximate memory usage reported in MB
- **Brute Force limitation**: Only run for N ≤ 20K to prevent OutOfMemory errors

---

## 4. Results

### 4.1 Uniform Random DNA

| N | K | BruteForce (ms) | Repeated | HashMap (ms) | Repeated | SortBased (ms) | Repeated |
|---|---|----------------|----------|--------------|----------|----------------|----------|
| 10K | 10  | 259.5 | 50   | 1.96  | 50   | 5.94  | 50   |
| 10K | 50  | 373.7 | 0    | 2.05  | 0    | 4.77  | 0    |
| 10K | 200 | 366.8 | 0    | 2.57  | 0    | 4.99  | 0    |
| 50K | 10  | skipped | -  | 4.61  | 1106 | 19.63 | 1106 |
| 50K | 50  | skipped | -  | 8.75  | 0    | 16.63 | 0    |
| 50K | 200 | skipped | -  | 8.79  | 0    | 20.53 | 0    |
| 100K| 10  | skipped | -  | 11.53 | 4344 | 40.90 | 4344 |
| 100K| 50  | skipped | -  | 14.10 | 0    | 35.02 | 0    |
| 100K| 200 | skipped | -  | 17.94 | 0    | 48.12 | 0    |

### 4.2 Highly Repetitive DNA

| N | K | BruteForce (ms) | Repeated | HashMap (ms) | Repeated | SortBased (ms) | Repeated |
|---|---|----------------|----------|--------------|----------|----------------|----------|
| 10K | 10  | 4442.6 | 4  | 0.28  | 4  | 1.69  | 4  |
| 10K | 50  | 5048.4 | 4  | 0.35  | 4  | 1.53  | 4  |
| 10K | 200 | 7406.2 | 4  | 0.44  | 4  | 1.61  | 4  |
| 50K | 10  | skipped | -  | 2.06  | 4  | 7.99  | 4  |
| 50K | 50  | skipped | -  | 2.20  | 4  | 6.74  | 4  |
| 50K | 200 | skipped | -  | 3.87  | 4  | 10.44 | 4  |
| 100K| 10  | skipped | -  | 3.44  | 4  | 13.89 | 4  |
| 100K| 50  | skipped | -  | 5.08  | 4  | 13.33 | 4  |
| 100K| 200 | skipped | -  | 5.95  | 4  | 18.97 | 4  |

### 4.3 GC-Biased DNA (70% GC)

| N | K | BruteForce (ms) | Repeated | HashMap (ms) | Repeated | SortBased (ms) | Repeated |
|---|---|----------------|----------|--------------|----------|----------------|----------|
| 10K | 10  | 283.7  | 166  | 0.60  | 166  | 3.91  | 166  |
| 10K | 50  | 282.3  | 0    | 1.72  | 0    | 3.23  | 0    |
| 10K | 200 | 272.4  | 0    | 1.67  | 0    | 4.08  | 0    |
| 50K | 10  | skipped | -  | 5.86  | 3738 | 21.20 | 3738 |
| 50K | 50  | skipped | -  | 12.10 | 0    | 16.95 | 0    |
| 50K | 200 | skipped | -  | 7.82  | 0    | 18.64 | 0    |
| 100K| 10  | skipped | -  | 10.78 | 11781| 44.73 | 11781|
| 100K| 50  | skipped | -  | 16.65 | 0    | 41.51 | 0    |
| 100K| 200 | skipped | -  | 20.47 | 0    | 50.59 | 0    |

---

## 5. Analysis

1. **Brute Force**
    - Correct but infeasible for N > 20K or highly repetitive sequences.
    - Execution time grows quadratically with N.
    - High memory usage for dense repeated K-mers (up to 522 MB observed).

2. **HashMap**
    - Fastest in nearly all cases for moderate K and N.
    - Performance slightly decreases with larger K due to substring hashing cost.
    - Handles highly repetitive sequences efficiently, low memory overhead relative to brute force.

3. **Sort-based**
    - Slightly slower than HashMap for moderate N and K due to object allocation and TimSort overhead.
    - Deterministic memory access; advantageous for extremely large K or N (e.g., genome-scale datasets).

4. **Patterns Observed**
    - Small K (K=10) → many repeats in highly repetitive and GC-biased DNA → HashMap and Sort-based efficient, brute force memory-intensive.
    - Large K (K≥50) → few repeats in uniform and GC-biased DNA → HashMap and Sort perform similarly; brute force unnecessary.
    - HashMap’s O(K) hidden cost is visible for larger K values; sort-based scales better for very large K/N but object overhead dominates at moderate sizes.

---

## 6. Correctness Verification

- For small N (≤10K), all three approaches report **identical repeated K-mers and positions**.
- Position lists were sorted, duplicates removed, cross-checked between approaches.
- Confirms algorithmic correctness before scaling to large N.

---

## 7. Conclusions

1. **Algorithm selection is context-dependent**:
    - Brute force is only practical for small N.
    - HashMap dominates for moderate N/K.
    - Sort-based useful for massive N or very large K.

2. **Memory vs Performance**:
    - Brute force consumes excessive memory on dense repeats.
    - HashMap and Sort-based memory usage similar (~2–7 MB at N=10K–100K, moderate K).

3. **Practical insights**:
    - HashMap’s “O(1)” lookup cost is actually O(K) for strings in Java.
    - JVM optimizations and caching patterns influence real-world performance.
    - High repetition patterns reveal brute force memory bottlenecks, but correctness validation is possible at small N.

4. **Overall recommendation**:
    - Use HashMap for standard sequence analysis.
    - Use Sort-based methods only for extreme-scale data or when memory access patterns dominate.
    - Always validate correctness on a small dataset using brute force before scaling.

