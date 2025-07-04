## \file text_processor.py
## \brief Processes multilingual texts by detecting language and extracting named entities using spaCy.
## \author Stefan Stan

import spacy
import json
from langdetect import detect

# Load spaCy models by language (Spanish, English, and French)
models = {
    'es': spacy.load("es_core_news_sm"),
    'en': spacy.load("en_core_web_sm"),
    'fr': spacy.load("fr_core_news_sm"),
}

def detect_language(text):
    '''
    @brief Detects the language of a text.
    @param text Text in string format to analyze.
    @return ISO 639-1 code of the detected language (e.g., 'es', 'en', 'fr').
    '''
    try:
        return detect(text)
    except:
        return 'es'  # Default value if detection fails

def tag_text(text):
    '''
    @brief Tags named entities in a text by automatically detecting the language.
    @param text Text to process.
    @return A tuple with the list of found entities [(text, type)] and the detected language.
    '''
    language = detect_language(text)
    model = models.get(language, models['es'])  # Use Spanish model if language is not supported
    doc = model(text)
    return [(ent.text, ent.label_) for ent in doc.ents], language

def extract_texts(data):
    '''
    @brief Extracts relevant text strings from the input JSON data.
    @param data JSON object (dict) with keys like title, h1, h2, h3, h4, p, etc.
    @return List of text strings extracted from the JSON.
    '''
    texts = []

    # Extract title if present
    if 'title' in data and data['title']:
        texts.append(data['title'])

    # Extract lists of strings from h1, h2, h3, h4, p keys
    for key in ['h1', 'h2', 'h3', 'h4', 'p']:
        if key in data and isinstance(data[key], list):
            texts.extend([item for item in data[key] if isinstance(item, str) and item.strip() != ""])

    return texts

def process_json(input_path, output_path):
    '''
    @brief Processes an input JSON file, tagging texts by language, and saves the results to another JSON.
    @param input_path Path to the input JSON file.
    @param output_path Path where the result JSON file will be saved.
    @return List of results with text, language, tags, and relevance (number of tags).
    '''
    with open(input_path, "r", encoding="utf-8") as f:
        data = json.load(f)

    # The input data is expected to be either a dict or a list of dicts
    # Handle both cases
    records = data if isinstance(data, list) else [data]

    results = []
    for record in records:
        texts = extract_texts(record)
        for text in texts:
            if not text.strip():
                continue
            tags, detected_language = tag_text(text)
            results.append({
                "text": text,
                "language": detected_language,
                "tags": tags,
                "relevance": len(tags)
            })

    # Sort results by number of named entities (relevance) descending
    results.sort(key=lambda x: x["relevance"], reverse=True)

    with open(output_path, "w", encoding="utf-8") as f:
        json.dump(results, f, ensure_ascii=False, indent=4)

    return results
