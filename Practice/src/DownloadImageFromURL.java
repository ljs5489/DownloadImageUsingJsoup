import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Scanner;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class DownloadImageFromURL {
	public static void main(String[] args) {

		DownloadImage temp = new DownloadImage();
		temp.setDirectory("F");
		temp.setFileName("");

		Scanner input = new Scanner(System.in);

		while (true) {

			System.out.print("폴더명: ");
			temp.setFolderName("down\\" + input.nextLine());

			int from = 1;
			String frontURL = "http://hitomi.la/reader/";
			System.out.print("만화넘버: ");
			String serialNum = input.nextLine();
			Document doc;

			String tempURL = frontURL + serialNum + ".html";
			System.out.println(tempURL);

			try {
				doc = Jsoup.connect(tempURL).get();
				Elements imgURL = doc.select(".img-url");

				for (int i = from;; i++) {
					String myImgURL = imgURL.get(i).text().replace("//", "");
					System.out.println(myImgURL);
					temp.setMyURL("http://" + myImgURL);
					// http를 꼭 붙여줘야 함!

					try {
						temp.setFileNum(i);
						temp.downGo();
					} catch (Exception e) {
						e.printStackTrace();
					}

				}
			} catch (IOException e1) {
				e1.printStackTrace();
			} catch (IndexOutOfBoundsException e2) {

			}

			// System.out.println(" : "+(double)(i*100/to)+"%");

			System.out.println("완료되었습니다.");
		}
	}

}

class DownloadImage {

	private String directory = "C";
	private String folderName = "test";
	private String fileName = "temp";
	private int fileNum = 1;

	private String myURL;
	private int from = 1;
	private int to;

	public void setTo(int to) {
		this.to = to;
	}

	public void setFrom(int from) {
		this.from = from;
	}

	public void setMyURL(String myURL) {
		this.myURL = myURL;
	}

	public void setFileNum(int fileNum) {
		this.fileNum = fileNum;
	}

	public void setFolderName(String folderName) {
		this.folderName = folderName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	public void setDirectory(String directory) {
		this.directory = directory;
	}

	public void setting() {
		Scanner input = new Scanner(System.in);

		System.out.println("디렉토리? (기본 C)");
		setTo(input.nextInt());

		System.out.println("폴더 명? (기본 test)");
		setFolderName(input.nextLine());

		System.out.println("파일이름? (기본 temp)");
		setFileName(input.nextLine());
	}

	public void downGo() throws Exception {
		URL url = new URL(myURL);
		mkdir();

		InputStream in = new BufferedInputStream(url.openStream());
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		byte[] buf = new byte[1024];
		int n = 0;
		while (-1 != (n = in.read(buf))) {
			out.write(buf, 0, n);
		}
		out.close();
		in.close();
		byte[] response = out.toByteArray();

		FileOutputStream fos = new FileOutputStream(directory + ":\\"
				+ folderName + "\\" + fileName + fileNum + ".jpg");
		fos.write(response);
		fos.close();
	}

	public void mkdir() {
		File file = new File(directory + ":\\" + folderName);
		if (!file.exists()) {
			if (file.mkdir()) {
				System.out.println("Directory is created!");
			}
		}

	}
}
