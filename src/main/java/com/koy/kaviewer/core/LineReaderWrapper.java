package com.koy.kaviewer.core;

import com.koy.kaviewer.core.annotation.GroupCommandRegistrar;
import org.jline.keymap.KeyMap;
import org.jline.reader.Binding;
import org.jline.reader.Buffer;
import org.jline.reader.EndOfFileException;
import org.jline.reader.Expander;
import org.jline.reader.Highlighter;
import org.jline.reader.History;
import org.jline.reader.LineReader;
import org.jline.reader.ParsedLine;
import org.jline.reader.Parser;
import org.jline.reader.UserInterruptException;
import org.jline.reader.Widget;
import org.jline.terminal.MouseEvent;
import org.jline.terminal.Terminal;

import java.util.Map;

public class LineReaderWrapper implements LineReader {

    private static final LineReaderWrapper instance = new LineReaderWrapper();

    public static LineReaderWrapper getInstance() {
        return instance;
    }

    private LineReader delegate;
    private GroupCommandRegistrar groupCommandRegistrar;

    private LineReaderWrapper() {
    }

    public void setDelegate(LineReader delegate) {
        this.delegate = delegate;
    }

    public void setGroupCommandRegistrar(GroupCommandRegistrar groupCommandRegistrar) {
        this.groupCommandRegistrar = groupCommandRegistrar;
    }

    public LineReader getDelegate() {
        return delegate;
    }

    @Override
    public Map<String, KeyMap<Binding>> defaultKeyMaps() {
        return delegate.defaultKeyMaps();
    }

    @Override
    public String readLine() throws UserInterruptException, EndOfFileException {
        return delegate.readLine();
    }

    @Override
    public String readLine(Character character) throws UserInterruptException, EndOfFileException {
        return delegate.readLine(character);
    }

    @Override
    public String readLine(String s) throws UserInterruptException, EndOfFileException {
        return delegate.readLine(s);
    }

    @Override
    public String readLine(String s, Character character) throws UserInterruptException, EndOfFileException {
        return delegate.readLine(s, character);
    }

    @Override
    public String readLine(String s, Character character, String s1) throws UserInterruptException, EndOfFileException {
        return delegate.readLine(s, character, s1);
    }

    @Override
    public String readLine(String s, String s1, Character character, String s2) throws UserInterruptException, EndOfFileException {
        return delegate.readLine(s, s1, character, s2);
    }

    @Override
    public void callWidget(String s) {

        delegate.callWidget(s);
    }

    @Override
    public Map<String, Object> getVariables() {
        return delegate.getVariables();
    }

    @Override
    public Object getVariable(String s) {
        return delegate.getVariables();
    }

    @Override
    public void setVariable(String s, Object o) {

        delegate.setVariable(s, o);
    }

    @Override
    public boolean isSet(Option option) {
        return delegate.isSet(option);
    }

    @Override
    public void setOpt(Option option) {
        delegate.setOpt(option);
    }

    @Override
    public void unsetOpt(Option option) {
        delegate.unsetOpt(option);
    }

    @Override
    public Terminal getTerminal() {
        return delegate.getTerminal();
    }

    @Override
    public Map<String, Widget> getWidgets() {
        return delegate.getWidgets();
    }

    @Override
    public Map<String, Widget> getBuiltinWidgets() {
        return delegate.getBuiltinWidgets();
    }

    @Override
    public Buffer getBuffer() {
        return delegate.getBuffer();
    }

    @Override
    public void runMacro(String s) {
        delegate.runMacro(s);
    }

    @Override
    public MouseEvent readMouseEvent() {
        return delegate.readMouseEvent();
    }

    @Override
    public History getHistory() {
        return delegate.getHistory();
    }

    @Override
    public Parser getParser() {
        return delegate.getParser();
    }

    @Override
    public Highlighter getHighlighter() {
        return delegate.getHighlighter();
    }

    @Override
    public Expander getExpander() {
        return delegate.getExpander();
    }

    @Override
    public Map<String, KeyMap<Binding>> getKeyMaps() {
        return delegate.getKeyMaps();
    }

    @Override
    public String getKeyMap() {
        return delegate.getKeyMap();
    }

    @Override
    public boolean setKeyMap(String s) {
        return delegate.setKeyMap(s);
    }

    @Override
    public KeyMap<Binding> getKeys() {
        return delegate.getKeys();
    }

    @Override
    public ParsedLine getParsedLine() {
        // should do some tricks on words
        return this.groupCommandRegistrar.trimGroupCommand(delegate.getParsedLine());
    }

    @Override
    public String getSearchTerm() {
        return delegate.getSearchTerm();
    }

    @Override
    public RegionType getRegionActive() {
        return delegate.getRegionActive();
    }

    @Override
    public int getRegionMark() {
        return delegate.getRegionMark();
    }
}
