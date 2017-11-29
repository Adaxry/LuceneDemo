import java.io.BufferedReader;
import java.io.File;
import java.nio.file.Paths;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.cn.smart.SmartChineseAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.TextField;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.queryparser.classic.ParseException;
import org.apache.lucene.store.FSDirectory;
/*CreateIndex 用于创建索引
 * 首先设置自己的索引创建路径 indexPath、待处理数据路径 dataPath、数据行数num_line
 * 运行完毕会在indexPath路径下生成索引文件，如需在已有索引的基础上添加新的数据，只需将dataPath改为新数据路径即可
 */
public class CreateIndex {
	public static String indexPath = "G:\\index";   
	public static String dataPath  = "C:\\Users\\Administrator\\Desktop\\sport.txt"; // 注意：ANSI编码
	public static long num_line = 1000800;         

	public static void main(String[] args) throws IOException, ParseException {

		Analyzer analyzer = new SmartChineseAnalyzer();  // 智能分词
		FSDirectory index = FSDirectory.open(Paths.get(indexPath));
		IndexWriterConfig config = new IndexWriterConfig(analyzer);
		config.setOpenMode(IndexWriterConfig.OpenMode.CREATE_OR_APPEND); //增量索引 
		IndexWriter writer = new IndexWriter(index, config);

		System.out.println("开始导入数据...");
		long startTime = System.currentTimeMillis();  //导入数据，总计消耗时间
		loadData(writer, dataPath);
		long endTime = System.currentTimeMillis();
		long loadTime = endTime - startTime;
		System.out.format("导入完成，用时%dms\n", loadTime);
		
		writer.close();
	}

/* 导入Path路径下的文件，并显示执行进度  */
	public static void loadData(IndexWriter w, String path) {  
		try { 
			File filename = new File(path);  
			InputStreamReader reader = new InputStreamReader(new FileInputStream(filename));   
			BufferedReader br = new BufferedReader(reader); 
			String line = br.readLine();  

			int counter = 0 ;
			while (line != null) {  
				addDoc(w, line);
				line = br.readLine();  //每次从文件中读取一行

				counter++;
				if(counter % 10000 == 0) {  //每导入10000行数据，展示进度
					double percentage = (double)counter/num_line * 100;
					System.out.format("导入%.2f%%\n", percentage);
				}
			}  	

			System.out.println("Done!");
			br.close();

		} catch (Exception e) {  
			e.printStackTrace();  
		}  
	}

	
/*导入数据原语，类似SQL中的insert，记录只有content字段*/
	private static void addDoc(IndexWriter w, String content) throws IOException 
	{
		Document doc = new Document();
		doc.add(new TextField("content", content, Field.Store.YES));
		w.addDocument(doc);
	}

}