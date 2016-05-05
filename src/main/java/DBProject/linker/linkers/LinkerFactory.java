package linker.linkers;

import linker.data.InvertedIndex;

public class LinkerFactory {

	public Linker createLinker(LinkerType type, InvertedIndex index) {
		if (type == null) return null;
		if (type == LinkerType.SIMPLE) return new SimpleLinker(index);
		if (type == LinkerType.DOC_NAME) return new DocumentNameLinker(index);
		if (type == LinkerType.STRICT) return new StrictLinker(index);
		return null;
	}
}
