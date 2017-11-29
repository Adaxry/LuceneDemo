import org.apache.lucene.analysis.cn.smart.SmartChineseAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.store.FSDirectory;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
/* Lucene�� ���ṩ��������ĺ���search
 * ��ָ����������·�� indexPath
 * ÿ�β�ѯ��󷵻ؽ���� hitsPerPage
 */
public class Lucene {

	public static String indexPath = "G:\\index";
	public static int hitsPerPage = 10;
	

	public static List<String> search(String str) { // str �������ؼ���
		SmartChineseAnalyzer analyzer = new SmartChineseAnalyzer();
		List<String> returnList = new ArrayList<String>();  // Ҫ���صĽ��

		String querystr = str.replaceAll(" ", "");
		if (querystr.length() == 0) {   //����Ϊ��ʱ��ֱ�ӷ���
			returnList.add("���������룺");
			analyzer.close();
			return returnList;
		}

		try { //ִ������
			Query q = new QueryParser("content", analyzer).parse(querystr);
			IndexReader reader = DirectoryReader.open(FSDirectory.open(Paths.get(indexPath)));
			long startTime = System.currentTimeMillis();  // avoid the open time
			IndexSearcher searcher = new IndexSearcher(reader);
			TopDocs docs = searcher.search(q, hitsPerPage);
			ScoreDoc[] hits = docs.scoreDocs;
			long endTime = System.currentTimeMillis();
			long searchTime = endTime - startTime;
			
			// �����¼�� ����ֵreturnList
			returnList.add("Time   :\t"+ searchTime + " ms");
			returnList.add("Found  :\t" + hits.length + " hits.");

			for(int i=0;i<hits.length;++i) {
				int docId = hits[i].doc;
				Document d = searcher.doc(docId);
				returnList.add(Integer.toString(i+1) + ".\t" + "score: " + String.format("%.6f", hits[i].score) + "\t" + d.get("content") );
			}
			// ÿ�����������󣬼���һ���ָ���
			returnList.add("------------------------------------------------------------------------------------------------");
			reader.close();

		} catch (Exception e) {
			System.out.println("Error searching " + str + " : " + e.getMessage());
		}
		return returnList;	
	}

}
