package diaryclient.main;

public class IconHandler {
	private static IconHandler _instance = null;
	
	private StringBuffer _sb = null;
	private int _start = 0;
	private int _end = 0;
	private int _offset = 0;
	private int _index = 0;
	
	private IconHandler() {
		_sb = new StringBuffer();
	}

	public static  IconHandler getInstance() {
		
		if (_instance == null) {
			_instance = new IconHandler();
		}
		return _instance;
	}
	
	public void clear() 
	{
		_sb.setLength(0);
		_start = 0;
		_end = 0;
		_offset=0;
		_index = -1;
	}
	
	public String getValue() {
		return _sb.toString();
	}
	
	public int getStart() {
		return _start;
	}
	
	public int getEnd() {
		return _end;
	}
	
	public int getOffset(){
		return _offset;
	}
	
	public boolean checkPattern(char c, int index)
	{
		System.out.println(String.format("input char is %c and index is %d.", c, index));
		
		boolean result = false;
		
		if ('/' == c) {
			clear();
			_sb.append('/');
			_start = index;
			_offset ++;
		} else if ('w' == c) {
			if(_sb.length() == 1) {
				_sb.append('w');
				_offset ++;
			} else {
				clear();
			}
			
		} else if ('0' <= c && c <= '9') {
			if(_sb.length() >= 2) {
				_sb.append(c);
				result = true;
				_end = index;
				_offset ++;
				_index = c-'0';
			} else {
				clear();
			}
		} else {
			clear();
		}
		
		return result;
	}

	public String getIconPath() {
		
		String icons[] = {"face-with-tears.png",
			"grinning-face.png",
			"neutral-face.png",
			"pensive-face.png",
			"smiling-face-with-sunglasses.png",
			"white-frowning-face.png"};
	
		if (_index >= 0 && _index < icons.length) {
			return icons[_index];
		} else {
			return null;
		}
	}
}
