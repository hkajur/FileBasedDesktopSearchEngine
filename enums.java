/*
 * Venkata Harish Kajur 8982
 * Jonathan Lysiak 4477
 * CS-345
 * Homework 6
 */

/*
 * Defined constants
 */
enum FileType {
    HTML,
    TEXT,
    NSTF
}

enum ParseTokenType {
    TOKEN,
    TOKENDEBUG
}

enum TokenStatus {
    REGULAR,
    INSIDE_TITLE,
    INSIDE_A,
    INSIDE_H1_3,
    INSIDE_H4_6
}

enum TokenType {
    START_STATE,
    WORD,
    USELESS_CHAR,
    BAD_TAG_NAME,
    WORD_SPACE,
    BANG,
    OPEN_GREAT,
    OPEN_GREAT_SPACE,
    TAG_NAME,
    SLASH_TAG_NAME,
    SLASH,
    CLOSE_GREAT
}
