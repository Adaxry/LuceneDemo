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
/* Lucene类 是提供搜索服务的函数search
 * 需指定索引所在路径 indexPath
 * 每次查询最大返回结果数 hitsPerPage
 */
public class Lucene {

	public static String indexPath = "G:\\index";
	public static int hitsPerPage = 10;
	

	public static List<String> search(String str) { // str 是搜索关键字
		SmartChineseAnalyzer analyzer = new SmartChineseAnalyzer();
		List<String> returnList = new ArrayList<String>();  // 要返回的结果

		String querystr = str.replaceAll(" ", "");
		if (querystr.length() == 0) {   //输入为空时，直接返回
			returnList.add("请重新输入：");
			analyzer.close();
			return returnList;
		}

		try { //执行搜索
			Query q = new QueryParser("content", analyzer).parse(querystr);
			IndexReader reader = DirectoryReader.open(FSDirectory.open(Paths.get(indexPath)));
			long startTime = System.currentTimeMillis();  // avoid the open time
			IndexSearcher searcher = new IndexSearcher(reader);
			TopDocs docs = searcher.search(q, hitsPerPage);
			ScoreDoc[] hits = docs.scoreDocs;
			long endTime = System.currentTimeMillis();
			long searchTime = endTime - startTime;
			
			// 结果记录至 返回值returnList
			returnList.add("Time   :\t"+ searchTime + " ms");
			returnList.add("Found  :\t" + hits.length + " hits.");

			for(int i=0;i<hits.length;++i) {
				int docId = hits[i].doc;
				Document d = searcher.doc(docId);
				returnList.add(Integer.toString(i+1) + ".\t" + "score: " + String.format("%.6f", hits[i].score) + "\t" + d.get("content") );
			}
			// 每次搜索结束后，加入一条分割线
			returnList.add("------------------------------------------------------------------------------------------------");
			reader.close();

		} catch (Exception e) {
			System.out.println("Error searching " + str + " : " + e.getMessage());
		}
		return returnList;	
	}

}
