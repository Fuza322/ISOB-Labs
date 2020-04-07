using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.IO;
using System.Threading.Tasks;

public class CodeOfCaesar
{
    private string CodeEncode(string text, int k)
    {
        const string alfabet = "АБВГДЕЁЖЗИЙКЛМНОПРСТУФХЦЧШЩЪЫЬЭЮЯ";
        
        var resultSymbol = "";
        var finalAlfabet = alfabet + alfabet.ToLower();
        var alfabetSize = finalAlfabet.Length;
       
        for (int i = 0; i < text.Length; i++)
        {
            var symbol = text[i];
            var index = finalAlfabet.IndexOf(symbol);

            if (index < 0)
            {
                resultSymbol += symbol.ToString();
            }
            else
            {
                var codeIndex = (alfabetSize + index + k) % alfabetSize;
                resultSymbol += finalAlfabet[codeIndex];
            }
        }
        return resultSymbol;
    }

    public string Encoder(string initMessage, int key) => CodeEncode(initMessage, key);

    public string Decoder(string encodedMessage, int key) => CodeEncode(encodedMessage, -key);
}

class Program
{
    static void Main(string[] args)
    {
        var cipher = new CodeOfCaesar();
        //string file_name = "initial.txt";
        //var initText = File.ReadAllText(file_name, Encoding.UTF8);
        Console.WriteLine("Введите исходные даннык: ");
        var initText = Console.ReadLine();
        Console.WriteLine();
        Console.WriteLine("********************** Шифр Цезаря **********************");
        Console.WriteLine();
        Console.WriteLine("----------------------------------------------------------");
        Console.WriteLine("Исходный текст: ");
        Console.WriteLine(initText);
        Console.WriteLine("----------------------------------------------------------");
        Console.Write("Введите ключ: ");
        var key = Convert.ToInt32(Console.ReadLine());
        Console.WriteLine("----------------------------------------------------------");
        var codedText = cipher.Encoder(initText, key);
        Console.WriteLine("####################### Результат #######################");
        Console.WriteLine();
        Console.WriteLine("Закодированный текст: {0}", codedText);
        Console.WriteLine("Декодированный текст: {0}", cipher.Decoder(codedText, key));
        Console.ReadLine();
    }
}