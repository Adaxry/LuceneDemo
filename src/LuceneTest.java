import java.io.File;
import java.io.IOException;
import org.apache.lucene.analysis.cn.smart.SmartChineseAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.queryparser.classic.ParseException;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.store.FSDirectory;
import java.io.FileWriter;
import java.nio.file.Paths;
import java.io.InputStreamReader;  
import java.io.BufferedReader;  

/*LuceneTest�Ǹ����Ժ�������ֱ����IDE�����У���ɱ�������exportPath*/
public class LuceneTest {

	public static String indexPath = "G:\\index";
	public static String exportPath = "G:\\export.txt";
	public static int hitsPerPage = 10;  // ��ѯ����ĸ�������
	public static void main(String[] args) throws IOException, ParseException {
		SmartChineseAnalyzer analyzer = new SmartChineseAnalyzer();
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		
		System.out.println("�������ѯ���(q �˳�): ");
		String s = br.readLine().replaceAll(" ", "");
		while (!s.equalsIgnoreCase("q") && s.length() > 0) {
			try {
				// ��ʼ��������
				long startTime = System.currentTimeMillis();
				String querystr = s;
				Query q = new QueryParser("content", analyzer).parse(querystr);
				IndexReader reader = DirectoryReader.open(FSDirectory.open(Paths.get(indexPath)));
				IndexSearcher searcher = new IndexSearcher(reader);
				TopDocs docs = searcher.search(q, hitsPerPage);
				ScoreDoc[] hits = docs.scoreDocs;
				long endTime = System.currentTimeMillis();
				long searchTime = endTime - startTime;
				System.out.format("��ʱ%dms\n",searchTime); //����
				
                // �����������				
				File file = new File(exportPath);
				FileWriter writer = new FileWriter(file, true); 
				writer.write("Search :\t" + querystr + "\n" ); 
				writer.write("Time   :\t" + searchTime + " ms\n" ); 
				writer.write("Found  :\t" + hits.length + " hits\n" );
				
				// ��ʾ�������
				System.out.println("Found " + hits.length + " hits.");
				for(int i=0;i<hits.length;++i) {
					int docId = hits[i].doc;
					Document d = searcher.doc(docId);
					System.out.println((i + 1) + ". " + "score: " + hits[i].score + "\n" + d.get("content") );
					writer.write((i+1) + ". score: " + hits[i].score + " " + d.get("content") + "\n"); 
					
				}
				writer.write("----------------------------------------------------------------------\n");
				writer.flush();
				writer.close();
				System.out.println("\n�������ѯ���(q �˳�): ");
				reader.close();
				s = br.readLine().replaceAll(" ", "");
				if (s.equalsIgnoreCase("q")) {
					break;
				}
			} catch (Exception e) {
				System.out.println("Error searching " + s + " : " + e.getMessage());
			}
			
		} //while
	} //main
} // class