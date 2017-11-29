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
/*CreateIndex ���ڴ�������
 * ���������Լ�����������·�� indexPath������������·�� dataPath����������num_line
 * ������ϻ���indexPath·�������������ļ������������������Ļ���������µ����ݣ�ֻ�轫dataPath��Ϊ������·������
 */
public class CreateIndex {
	public static String indexPath = "G:\\index";   
	public static String dataPath  = "C:\\Users\\Administrator\\Desktop\\sport.txt"; // ע�⣺ANSI����
	public static long num_line = 1000800;         

	public static void main(String[] args) throws IOException, ParseException {

		Analyzer analyzer = new SmartChineseAnalyzer();  // ���ִܷ�
		FSDirectory index = FSDirectory.open(Paths.get(indexPath));
		IndexWriterConfig config = new IndexWriterConfig(analyzer);
		config.setOpenMode(IndexWriterConfig.OpenMode.CREATE_OR_APPEND); //�������� 
		IndexWriter writer = new IndexWriter(index, config);

		System.out.println("��ʼ��������...");
		long startTime = System.currentTimeMillis();  //�������ݣ��ܼ�����ʱ��
		loadData(writer, dataPath);
		long endTime = System.currentTimeMillis();
		long loadTime = endTime - startTime;
		System.out.format("������ɣ���ʱ%dms\n", loadTime);
		
		writer.close();
	}

/* ����Path·���µ��ļ�������ʾִ�н���  */
	public static void loadData(IndexWriter w, String path) {  
		try { 
			File filename = new File(path);  
			InputStreamReader reader = new InputStreamReader(new FileInputStream(filename));   
			BufferedReader br = new BufferedReader(reader); 
			String line = br.readLine();  

			int counter = 0 ;
			while (line != null) {  
				addDoc(w, line);
				line = br.readLine();  //ÿ�δ��ļ��ж�ȡһ��

				counter++;
				if(counter % 10000 == 0) {  //ÿ����10000�����ݣ�չʾ����
					double percentage = (double)counter/num_line * 100;
					System.out.format("����%.2f%%\n", percentage);
				}
			}  	

			System.out.println("Done!");
			br.close();

		} catch (Exception e) {  
			e.printStackTrace();  
		}  
	}

	
/*��������ԭ�����SQL�е�insert����¼ֻ��content�ֶ�*/
	private static void addDoc(IndexWriter w, String content) throws IOException 
	{
		Document doc = new Document();
		doc.add(new TextField("content", content, Field.Store.YES));
		w.addDocument(doc);
	}

}