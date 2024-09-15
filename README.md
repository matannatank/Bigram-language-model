# Bigram Model Language Processing Project

## Overview

This project implements a **Bigram Model** in Java, which processes text data to build a vocabulary and calculate bigram probabilities between words. The bigram model is a statistical language model that predicts the next word in a sequence given the previous word. It is often used in natural language processing tasks, such as text prediction and language modeling.

The project was part of a university course focused on software engineering and data processing ("software 1"). It includes functions to build the vocabulary from a text file, compute bigram counts, and use various metrics like cosine similarity for word relationships.

## Features

- **Vocabulary Building**: Extracts unique words from a text file while considering both numbers and words.
- **Bigram Count Matrix**: Calculates how often a word follows another word in the input text.
- **Cosine Similarity**: Computes the cosine similarity between word vectors to find the most similar words.
- **Sentence Validation**: Checks if a given sentence is "legal" based on the bigram model.
- **Most Frequent Word Prediction**: Finds the most frequent word following a given word in the text.
- **Save/Load Model**: Saves the vocabulary and bigram counts to files and loads them for later use.

## Process

1. **Text Processing**: The program reads a file, splits the content into words, and constructs a vocabulary based on English letters and valid numbers.
2. **Bigram Counting**: The model tracks how often each word follows another word, storing these counts in a matrix.
3. **Cosine Similarity**: It calculates the similarity between word vectors using cosine similarity to find words that often appear in similar contexts.
4. **Sentence Validation**: The model checks if a sentence consists of valid bigram sequences from the training data.
5. **Model Saving and Loading**: The vocabulary and bigram counts can be saved to files for reuse in future sessions.

## Files

- `BigramModel.java`: The main file containing all the functions for vocabulary building, bigram counting, sentence validation, and cosine similarity calculation.

## How to Run

1. Compile the Java file using the following command:
   ```bash
   javac BigramModel.java
