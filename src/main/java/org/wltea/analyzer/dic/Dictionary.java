/**
 *
 */
package org.wltea.analyzer.dic;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Collection;
import java.util.List;

import org.wltea.analyzer.cfg.Configuration;

/**
 * IK Analyzer v3.2
 * 词典管理类,单子模式
 * @author 林良益
 *
 */
public class Dictionary {
    /*
     * 分词器默认字典路径
     */
    public static final String PATH_DIC_MAIN = "/org/wltea/analyzer/dic/main.dic";
    public static final String PATH_DIC_SURNAME = "/org/wltea/analyzer/dic/surname.dic";
    public static final String PATH_DIC_QUANTIFIER = "/org/wltea/analyzer/dic/quantifier.dic";
    public static final String PATH_DIC_SUFFIX = "/org/wltea/analyzer/dic/suffix.dic";
    public static final String PATH_DIC_PREP = "/org/wltea/analyzer/dic/preposition.dic";
    public static final String PATH_DIC_STOP = "/org/wltea/analyzer/dic/stopword.dic";


    /*
     * 词典单子实例
     */
    private static final Dictionary singleton;

    /*
     * 词典初始化
     */
    static {
        singleton = new Dictionary();
    }

    /*
     * 主词典对象
     */
    private DictSegment _MainDict;
    /*
     * 姓氏词典
     */
    private DictSegment _SurnameDict;
    /*
     * 量词词典
     */
    private DictSegment _QuantifierDict;
    /*
     * 后缀词典
     */
    private DictSegment _SuffixDict;
    /*
     * 副词，介词词典
     */
    private DictSegment _PrepDict;
    /*
     * 停止词集合
     */
    private DictSegment _StopWords;

    private Dictionary() {
        //初始化系统词典
//        loadMainDict(null);
//		loadSurnameDict();
//		loadQuantifierDict();
//		loadSuffixDict();
//		loadPrepDict();
        loadStopWordDict();
    }

    /**
     * 加载主词典及扩展词典
     */
    public void loadMainDict(List<String> customerDict) {
        //建立一个主词典实例
        _MainDict = new DictSegment((char) 0);
        //读取主词典文件
        InputStream is = Dictionary.class.getResourceAsStream(Dictionary.PATH_DIC_MAIN);
        if (is == null) {
            throw new RuntimeException("Main Dictionary not found!!!");
        }

        //加载主词典数据
//		loadDict(_MainDict, is, "Main");

        //加载扩展词典配置
        List<String> extDictFiles = Configuration.getExtDictionarys();
        for (String extDictName : extDictFiles) {
            //读取扩展词典文件
            is = Dictionary.class.getResourceAsStream(extDictName);
            //如果找不到扩展的字典，则忽略
            if (is == null) {
                continue;
            }
            //加载扩展词典数据
            loadDict(_MainDict, is, "Extension");
        }

        //加载自定义词典
        if (customerDict != null && customerDict.size() > 0) {
			System.out.println("加载自定义词典");
            loadCustomerDict(_MainDict, customerDict);
        }
    }

    /**
     * 加载词典数据
     */
    private void loadDict(DictSegment dict, InputStream inputStream, String dictType) {
        try {
            BufferedReader br = new BufferedReader(new InputStreamReader(inputStream, "UTF-8"), 512);
            String theWord = null;
            do {
                theWord = br.readLine();
                if (theWord != null && !"".equals(theWord.trim())) {
                    //加载词典数据到主内存词典中
                    dict.fillSegment(theWord.trim().toCharArray());
                }
            } while (theWord != null);

        } catch (IOException ioe) {
            System.err.println(dictType + "Dictionary loading exception.");
            ioe.printStackTrace();
        } finally {
            try {
                if (inputStream != null) {
                    inputStream.close();
                    inputStream = null;
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

	/**
	 * 加载自定义词典
	 * @param dict
	 * @param customerDict
	 */
	private void loadCustomerDict(DictSegment dict, List<String> customerDict) {
        for (String theWord : customerDict) {
			if (theWord != null && !"".equals(theWord.trim())) {
				//加载词典数据到主内存词典中
				dict.fillSegment(theWord.trim().toCharArray());
			}
        }
    }

    /**
     * 加载姓氏词典
     */
    private void loadSurnameDict() {
        //建立一个姓氏词典实例
        _SurnameDict = new DictSegment((char) 0);
        //读取姓氏词典文件
        InputStream is = Dictionary.class.getResourceAsStream(Dictionary.PATH_DIC_SURNAME);
        if (is == null) {
            throw new RuntimeException("Surname Dictionary not found!!!");
        }
        //加载姓氏词典数据
        loadDict(_SurnameDict, is, "Surname");
    }

    /**
     * 加载量词词典
     */
    private void loadQuantifierDict() {
        //建立一个量词典实例
        _QuantifierDict = new DictSegment((char) 0);
        //读取量词词典文件
        InputStream is = Dictionary.class.getResourceAsStream(Dictionary.PATH_DIC_QUANTIFIER);
        if (is == null) {
            throw new RuntimeException("Quantifier Dictionary not found!!!");
        }
        //加载量词词典数据
        loadDict(_QuantifierDict, is, "Quantifier");
    }

    /**
     * 加载后缀词典
     */
    private void loadSuffixDict() {
        //建立一个后缀词典实例
        _SuffixDict = new DictSegment((char) 0);
        //读取后缀词典文件
        InputStream is = Dictionary.class.getResourceAsStream(Dictionary.PATH_DIC_SUFFIX);
        if (is == null) {
            throw new RuntimeException("Suffix Dictionary not found!!!");
        }
        //加载后缀词典数据
        loadDict(_SuffixDict, is, "Suffix");
    }

    /**
     * 加载介词\副词词典
     */
    private void loadPrepDict() {
        //建立一个介词\副词词典实例
        _PrepDict = new DictSegment((char) 0);
        //读取介词\副词词典文件
        InputStream is = Dictionary.class.getResourceAsStream(Dictionary.PATH_DIC_PREP);
        if (is == null) {
            throw new RuntimeException("Preposition Dictionary not found!!!");
        }
        //加载介词\副词词典数据
        loadDict(_PrepDict, is, "Preposition");
    }

    /**
     * 加载停止词词典
     */
    private void loadStopWordDict() {
        //建立一个停止词典实例
        _StopWords = new DictSegment((char) 0);
        //读取量词词典文件
        InputStream is = Dictionary.class.getResourceAsStream(Dictionary.PATH_DIC_STOP);
        if (is == null) {
            throw new RuntimeException("StopWord Dictionary not found!!!");
        }
        //加载主停止词典数据
        loadDict(_StopWords, is, "StopWord");

        //加载扩展停止词典
        List<String> extStopWordDictFiles = Configuration.getExtStopWordDictionarys();
        for (String extStopWordDictName : extStopWordDictFiles) {
            //读取扩展词典文件
            is = Dictionary.class.getResourceAsStream(extStopWordDictName);
            //如果找不到扩展的字典，则忽略
            if (is == null) {
                continue;
            }
            //加载词典数据
            loadDict(_StopWords, is, "ExtensionStopWord");
        }
    }

    /**
     * 词典初始化
     * 由于IK Analyzer的词典采用Dictionary类的静态方法进行词典初始化
     * 只有当Dictionary类被实际调用时，才会开始载入词典，
     * 这将延长首次分词操作的时间
     * 该方法提供了一个在应用加载阶段就初始化字典的手段
     * 用来缩短首次分词时的时延
     * @return Dictionary
     */
    public static Dictionary getInstance() {
        return Dictionary.singleton;
    }

    /**
     * 加载扩展的词条
     * @param extWords Collection<String>词条列表
     */
    public static void loadExtendWords(Collection<String> extWords) {
        if (extWords != null) {
            for (String extWord : extWords) {
                if (extWord != null) {
                    //加载扩展词条到主内存词典中
                    singleton._MainDict.fillSegment(extWord.trim().toCharArray());
                }
            }
        }
    }

    /**
     * 加载扩展的停止词条
     * @param extStopWords Collection<String>词条列表
     */
    public static void loadExtendStopWords(Collection<String> extStopWords) {
        if (extStopWords != null) {
            for (String extStopWord : extStopWords) {
                if (extStopWord != null) {
                    //加载扩展的停止词条
                    singleton._StopWords.fillSegment(extStopWord.trim().toCharArray());
                }
            }
        }
    }

    /**
     * 检索匹配主词典
     * @param charArray
     * @return Hit 匹配结果描述
     */
    public static Hit matchInMainDict(char[] charArray) {
        return singleton._MainDict.match(charArray);
    }

    /**
     * 检索匹配主词典
     * @param charArray
     * @param begin
     * @param length
     * @return Hit 匹配结果描述
     */
    public static Hit matchInMainDict(char[] charArray, int begin, int length) {
        return singleton._MainDict.match(charArray, begin, length);
    }

    /**
     * 检索匹配主词典,
     * 从已匹配的Hit中直接取出DictSegment，继续向下匹配
     * @param charArray
     * @param currentIndex
     * @param matchedHit
     * @return Hit
     */
    public static Hit matchWithHit(char[] charArray, int currentIndex, Hit matchedHit) {
        DictSegment ds = matchedHit.getMatchedDictSegment();
        return ds.match(charArray, currentIndex, 1, matchedHit);
    }

    /**
     * 检索匹配姓氏词典
     * @param charArray
     * @param begin
     * @param length
     * @return Hit 匹配结果描述
     */
    public static Hit matchInSurnameDict(char[] charArray, int begin, int length) {
        return singleton._SurnameDict.match(charArray, begin, length);
    }

//	/**
//	 * 
//	 * 在姓氏词典中匹配指定位置的char数组
//	 * （对传入的字串进行后缀匹配）
//	 * @param charArray
//	 * @param begin
//	 * @param end
//	 * @return
//	 */
//	public static boolean endsWithSurnameDict(char[] charArray , int begin, int length){
//		Hit hit = null;
//		for(int i = 1 ; i <= length ; i++){
//			hit = singleton._SurnameDict.match(charArray, begin + (length - i) , i);
//			if(hit.isMatch()){
//				return true;
//			}
//		}
//		return false;
//	}

    /**
     * 检索匹配量词词典
     * @param charArray
     * @param begin
     * @param length
     * @return Hit 匹配结果描述
     */
    public static Hit matchInQuantifierDict(char[] charArray, int begin, int length) {
        return singleton._QuantifierDict.match(charArray, begin, length);
    }

    /**
     * 检索匹配在后缀词典
     * @param charArray
     * @param begin
     * @param length
     * @return Hit 匹配结果描述
     */
    public static Hit matchInSuffixDict(char[] charArray, int begin, int length) {
        return singleton._SuffixDict.match(charArray, begin, length);
    }

//	/**
//	 * 在后缀词典中匹配指定位置的char数组
//	 * （对传入的字串进行前缀匹配）
//	 * @param charArray
//	 * @param begin
//	 * @param end
//	 * @return
//	 */
//	public static boolean startsWithSuffixDict(char[] charArray , int begin, int length){
//		Hit hit = null;
//		for(int i = 1 ; i <= length ; i++){
//			hit = singleton._SuffixDict.match(charArray, begin , i);
//			if(hit.isMatch()){
//				return true;
//			}else if(hit.isUnmatch()){
//				return false;
//			}
//		}
//		return false;
//	}

    /**
     * 检索匹配介词、副词词典
     * @param charArray
     * @param begin
     * @param length
     * @return Hit 匹配结果描述
     */
    public static Hit matchInPrepDict(char[] charArray, int begin, int length) {
        return singleton._PrepDict.match(charArray, begin, length);
    }

    /**
     * 判断是否是停止词
     * @param charArray
     * @param begin
     * @param length
     * @return boolean
     */
    public static boolean isStopWord(char[] charArray, int begin, int length) {
        return singleton._StopWords.match(charArray, begin, length).isMatch();
    }
}
