import java.awt.Font;
import java.awt.event.*;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import javax.swing.*;
/*swing 是创建GUI的类*/
public class swing  {  
	public static List<String> results = new ArrayList<String>(); //搜索结果
	public static JTextField searchText = new JTextField();       //搜索框   
	public static JTextArea resultText = new JTextArea();         //结果显示区
	
	public static void main(String[] args) { 
		JFrame f = new JFrame("检索系统");
		
		Font searchFont = new Font("Microsoft YaHei", Font.PLAIN, 22);
		searchText.setFont(searchFont);
		searchText.setBounds(50,50, 1100,50);

		Font resultFont = new Font("song", Font.BOLD, 20);
		resultText.setFont(resultFont);
		resultText.setBounds(50,120, 1250,518); 
		resultText.setLineWrap(true);

		JButton b_search=new JButton(new ImageIcon("image/search.png")); //搜索按钮    
		b_search.setBounds(1200,50,50,50);  
		b_search.addActionListener(new ActionListener(){ 
			public void actionPerformed(ActionEvent e){  
				results = Lucene.search(searchText.getText());
				resultText.setText(List2Str(results));
			}  
		});  
		
		JButton b_save=new JButton(new ImageIcon("image/save.png"));   //保存结果按钮
		b_save.setBounds(1280,50,50,50); 
		b_save.addActionListener(new ActionListener(){  
			public void actionPerformed(ActionEvent e){  
				JFileChooser fileChooser = new JFileChooser();
				if (fileChooser.showSaveDialog(f) == JFileChooser.APPROVE_OPTION) {
					try {
						saveResults(fileChooser.getSelectedFile());    
					} catch (IOException e1) {
						e1.printStackTrace();
					}
				}
			}  
		}); 
		
        //lunch frame
		f.add(b_search);
		f.add(b_save);
		f.add(searchText);
		f.add (resultText);
		f.setExtendedState(JFrame.MAXIMIZED_BOTH); 
		f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		f.setLayout(null);  
		f.setVisible(true);  
	}  

	/*将搜索结果存放至file中*/
	public static void saveResults(File file) throws IOException {
		FileWriter writer = new FileWriter(file, true);
		writer.write("Search :\t" + searchText.getText() + "\n" ); 
		for (String line : results) {
			writer.write(line + "\n");
		}
		writer.close();
	}
	
    /*将字符串数组合并为一条字符串的函数*/
	public static String List2Str(List<String> list) {
		String returnString = "";
		for (String line : list) {
			returnString += line + "\n" ;
		}
		return returnString;
	}

}  