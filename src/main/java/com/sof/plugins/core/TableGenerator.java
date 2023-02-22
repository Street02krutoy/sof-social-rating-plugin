package com.sof.plugins.core;

import net.md_5.bungee.api.chat.TextComponent;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

// Copyright by FisheyLP, Version 1.3 (12.08.16)
public class TableGenerator {

    private static String delimiter = " | ";
    private static final List<Character> char7 = Arrays.asList('°', '~', '@');
    private static final List<Character> char5 = Arrays.asList('"', '{', '}', '(', ')', '*', 'f', 'k', '<', '>');
    private static final List<Character> char4 = Arrays.asList('I', 't', ' ', '[', ']', '€');
    private static final List<Character> char3 = Arrays.asList('l', '`', '³', '\'');
    private static final List<Character> char2 = Arrays.asList(',', '.', '!', 'i', '´', ':', ';', '|');
    private static final char char1 = '៲';
    private static String colors = "[&§][0-9a-fA-Fk-oK-OrR]";
    private final Alignment[] alignments;
    private final List<Row> table = new ArrayList<>();
    private final int columns;

    public TableGenerator(Alignment... alignments) {
        if (alignments == null || alignments.length < 1)
            throw new IllegalArgumentException("Must atleast provide 1 alignment.");

        this.columns = alignments.length;
        this.alignments = alignments;
    }

    public List<TextComponent> generate(Receiver receiver, boolean ignoreColors) {
        if (receiver == null) {
            throw new IllegalArgumentException("Receiver must not be null.");
        }

        Integer[] columWidths = new Integer[columns];

        for (Row r : table) {
            for (int i = 0; i < columns; i++) {
                TextComponent text = r.texts.get(i);
                int length;

                if (ignoreColors)
                    length = getCustomLength(text.getText().replaceAll(colors, ""),
                            receiver);
                else length = getCustomLength(text.getText(), receiver);

                if (columWidths[i] == null) {
                    columWidths[i] = length;
                }

                else if (length > columWidths[i]) {
                    columWidths[i] = length;
                }
            }
        }

        List<TextComponent> lines = new ArrayList<TextComponent>();

        for (Row r : table) {
            TextComponent sb = new TextComponent();

            if (r.empty) {
                lines.add(new TextComponent(""));
                continue;
            }

            for (int i = 0; i < columns; i++) {
                Alignment agn = alignments[i];
                TextComponent text = r.texts.get(i);
                int length;

                if (ignoreColors)
                    length = getCustomLength(text.getText().replaceAll(colors, ""),
                            receiver);
                else length = getCustomLength(text.getText(),
                        receiver);

                int empty = columWidths[i] - length;
                int spacesAmount = empty;
                if (receiver == Receiver.CLIENT)
                    spacesAmount = (int) Math.floor(empty / 4d);

                String spaces = concatChars(' ', spacesAmount);

                if (agn == Alignment.LEFT) {
                    sb.addExtra(text);
                    if (i < columns - 1)
                        sb.addExtra(spaces);
                }
                if (agn == Alignment.RIGHT) {
                    sb.addExtra(spaces); sb.addExtra(text);
                }
                if (agn == Alignment.CENTER) {
                    int leftAmount = empty / 2;
                    int rightAmount = empty - leftAmount;

                    int spacesLeftAmount = leftAmount;
                    int spacesRightAmount = rightAmount;
                    if (receiver == Receiver.CLIENT) {
                        spacesLeftAmount = (int) Math.floor(spacesLeftAmount / 4d);
                        spacesRightAmount = (int) Math.floor(spacesRightAmount / 4d);
                    }

                    String spacesLeft = concatChars(' ', spacesLeftAmount);
                    String spacesRight = concatChars(' ', spacesRightAmount);

                    sb.addExtra(spacesLeft);
                    sb.addExtra(text);
                    if (i < columns - 1)
                        sb.addExtra(spacesRight);
                }

                if (i < columns - 1) sb.addExtra(delimiter);
            }
            lines.add(sb);
        }
        return lines;
    }

    public Paginator generateWithPaginate(Receiver receiver, boolean ignoreColors, int page) {
        if (receiver == null) {
            throw new IllegalArgumentException("Receiver must not be null.");
        }

        Integer[] columWidths = new Integer[columns];

        for (Row r : table) {
            for (int i = 0; i < columns; i++) {
                TextComponent text = r.texts.get(i);
                int length;

                if (ignoreColors)
                    length = getCustomLength(text.getText().replaceAll(colors, ""),
                            receiver);
                else length = getCustomLength(text.getText(), receiver);

                if (columWidths[i] == null) {
                    columWidths[i] = length;
                }

                else if (length > columWidths[i]) {
                    columWidths[i] = length;
                }
            }
        }

        List<TextComponent> lines = new ArrayList<TextComponent>();

        for (Row r : table) {
            TextComponent sb = new TextComponent();

            if (r.empty) {
                lines.add(new TextComponent(""));
                continue;
            }

            for (int i = 0; i < columns; i++) {
                Alignment agn = alignments[i];
                TextComponent text = r.texts.get(i);
                int length;

                if (ignoreColors)
                    length = getCustomLength(text.getText().replaceAll(colors, ""),
                            receiver);
                else length = getCustomLength(text.getText(),
                        receiver);

                int empty = columWidths[i] - length;
                int spacesAmount = empty;
                if (receiver == Receiver.CLIENT)
                    spacesAmount = (int) Math.floor(empty / 4d);

                String spaces = concatChars(' ', spacesAmount);

                if (agn == Alignment.LEFT) {
                    sb.addExtra(text);
                    if (i < columns - 1)
                        sb.addExtra(spaces);
                }
                if (agn == Alignment.RIGHT) {
                    sb.addExtra(spaces); sb.addExtra(text);
                }
                if (agn == Alignment.CENTER) {
                    int leftAmount = empty / 2;
                    int rightAmount = empty - leftAmount;

                    int spacesLeftAmount = leftAmount;
                    int spacesRightAmount = rightAmount;
                    if (receiver == Receiver.CLIENT) {
                        spacesLeftAmount = (int) Math.floor(spacesLeftAmount / 4d);
                        spacesRightAmount = (int) Math.floor(spacesRightAmount / 4d);
                    }

                    String spacesLeft = concatChars(' ', spacesLeftAmount);
                    String spacesRight = concatChars(' ', spacesRightAmount);

                    sb.addExtra(spacesLeft);
                    sb.addExtra(text);
                    if (i < columns - 1)
                        sb.addExtra(spacesRight);
                }

                if (i < columns - 1) sb.addExtra(delimiter);
            }
            lines.add(sb);
        }
        return new Paginator (lines);
    }

    protected static int getCustomLength(String text, Receiver receiver) {
        if (text == null) {
            throw new IllegalArgumentException("Text must not be null.");
        }
        if (receiver == null) {
            throw new IllegalArgumentException("Receiver must not be null.");
        }
        if (receiver == Receiver.CONSOLE) return text.length();

        int length = 0;
        for (char c : text.toCharArray())
            length += getCustomCharLength(c);

        return length;
    }

    protected static int getCustomCharLength(char c) {
        if (char1 == c) return 1;
        if (char2.contains(c)) return 2;
        if (char3.contains(c)) return 3;
        if (char4.contains(c)) return 4;
        if (char5.contains(c)) return 5;
        if (char7.contains(c)) return 7;

        return 6;
    }

    protected String concatChars(char c, int length) {
        String s = "";
        if (length < 1) return s;

        for (int i = 0; i < length; i++)
            s += Character.toString(c);
        return s;
    }

    public void addRow(TextComponent... texts) {
        if (texts == null) {
            throw new IllegalArgumentException("Texts must not be null.");
        }
        if (texts.length > columns) {
            throw new IllegalArgumentException("Too big for the table.");
        }

        Row r = new Row(texts);

        table.add(r);
    }

    public void addRow(String... texts) {
        if (texts == null) {
            throw new IllegalArgumentException("Texts must not be null.");
        }
        if (texts.length > columns) {
            throw new IllegalArgumentException("Too big for the table.");
        }

        Row r = new Row(texts);

        table.add(r);
    }

    private class Paginator {
        private List<TextComponent> lines;
        private int pageNumber;
        private int totalPages;

        protected Paginator (List<TextComponent> lines, int pageNumber) {
            this.lines = lines.subList()

        }
    }

    private class Row {

        public List<TextComponent> texts = new ArrayList<>();
        public boolean empty = true;

        public Row(TextComponent... texts) {
            if (texts == null) {
                for (int i = 0; i < columns; i++)
                    this.texts.add(new TextComponent(""));
                return;
            }

            for (TextComponent text : texts) {
                if (text != null && !text.getText().isEmpty()) empty = false;

                this.texts.add(text);
            }

            for (int i = 0; i < columns; i++) {
                if (i >= texts.length) this.texts.add(new TextComponent(""));
            }
        }

        public Row(String... texts) {
            if (texts == null) {
                for (int i = 0; i < columns; i++)
                    this.texts.add(new TextComponent(""));
                return;
            }

            for (String stringText : texts) {
                TextComponent text = new TextComponent(stringText);
                if (text != null && !text.getText().isEmpty()) empty = false;

                this.texts.add(text);
            }

            for (int i = 0; i < columns; i++) {
                if (i >= texts.length) this.texts.add(new TextComponent(""));
            }
        }
    }

    public enum Receiver {

        CONSOLE, CLIENT
    }

    public enum Alignment {

        CENTER, LEFT, RIGHT
    }
}