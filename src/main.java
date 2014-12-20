import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.Arrays;

public class main
{
	public static void main(String[] args)
	{
		ArrayList<String> keys = new ArrayList<String>();
		ArrayList<String> languages = new ArrayList<String>();
		ArrayList<ArrayList<String>> translations = new ArrayList<ArrayList<String>>();

		String fileData = readFile("trans.tsv");//args[0]);
		String[] lines = fileData.split("[\n]");

		for (int index = 0; index < lines.length; index++)
		{
			String[] columns = lines[index].split("\\t", -1);
			//for (int index = 2; index < columns.length; index++)
			{
				if (index == 0)
				{
					languages.addAll(Arrays.asList(Arrays.copyOfRange(columns, 1, columns.length)));
				}
				else
				{
					keys.add(columns[0]);

					for (int transIndex = 1; transIndex < columns.length; transIndex++)
					{
						if (translations.size() - 1 < transIndex - 1)
						{
							translations.add(transIndex - 1, new ArrayList<String>());
						}

						String str = columns[transIndex].trim();
						str = str == null ? "" : str;
						translations.get(transIndex - 1).add(str);
					}
				}
			}
		}

		for (int languageIndex = 0; languageIndex < languages.size(); languageIndex++)
		{
			String xmlStr = "<resources>\r\n";

			for (int keyIndex = 0; keyIndex < keys.size(); keyIndex++)
			{
				String translation = translations.get(languageIndex).get(keyIndex).trim();
				if (!translation.equals(""))
				{
					xmlStr += "\t<string name=\"" + keys.get(keyIndex) + "\"" + (translation.contains("%s") ? " formatted=\"false\"" : "") + ">" + translation + "</string>\r\n";
				}
			}

			xmlStr += "</resources>";
			writeFile("strings-" + languages.get(languageIndex).toLowerCase() + ".xml", xmlStr);
		}
	}

	public static void writeFile(String filename, String content)
	{
		try
		{
			File file = new File(filename);
			file.delete();
			OutputStreamWriter os = new OutputStreamWriter(new FileOutputStream(file), "UTF-8");
			os.write(content);
			os.flush();
			os.close();
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}

	public static String readFile(String filename)
	{
		try
		{
			String str = "";
			FileInputStream is = new FileInputStream(new File(filename));

			byte[] buff = new byte[8192];
			int len = 0;
			while ((len = is.read(buff)) != -1)
			{
				str += new String(buff, 0, len, "UTF-8");
			}

			return str;
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}

		return "";
	}
}