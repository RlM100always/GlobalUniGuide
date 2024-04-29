package com.techtravelcoder.alluniversityinformations.vocabulary;

import java.util.ArrayList;

public class WordResult {
    public String word;
    public String phonetic;
    public ArrayList<Meaning> meanings;

    public String getWord() {
        return word;
    }

    public void setWord(String word) {
        this.word = word;
    }

    public String getPhonetic() {
        return phonetic;
    }

    public void setPhonetic(String phonetic) {
        this.phonetic = phonetic;
    }

    public ArrayList<Meaning> getMeanings() {
        return meanings;
    }

    public void setMeanings(ArrayList<Meaning> meanings) {
        this.meanings = meanings;
    }
}
 class Definition{
    public String definition;

     public String getDefinition() {
         return definition;
     }

     public void setDefinition(String definition) {
         this.definition = definition;
     }
 }
 class Meaning{
    public String partOfSpeech;
    public ArrayList<Definition> definitions;
    public ArrayList<String> synonyms;
    public ArrayList<String> antonyms;

     public String getPartOfSpeech() {
         return partOfSpeech;
     }

     public void setPartOfSpeech(String partOfSpeech) {
         this.partOfSpeech = partOfSpeech;
     }

     public ArrayList<Definition> getDefinitions() {
         return definitions;
     }

     public void setDefinitions(ArrayList<Definition> definitions) {
         this.definitions = definitions;
     }

     public ArrayList<String> getSynonyms() {
         return synonyms;
     }

     public void setSynonyms(ArrayList<String> synonyms) {
         this.synonyms = synonyms;
     }

     public ArrayList<String> getAntonyms() {
         return antonyms;
     }

     public void setAntonyms(ArrayList<String> antonyms) {
         this.antonyms = antonyms;
     }
 }

