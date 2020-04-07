using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.IO;
using System.Threading.Tasks;

public class CodeOfVigenere
{
    public CodeOfVigenere(string alphabet = null)
    {
        letters = string.IsNullOrEmpty(alphabet) ? CodeOfVigenere.alphabet : alphabet;
    }

    const string alphabet = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
    readonly string letters;

    private string Vigenere(string text, string password, bool encrypting = true)
    {
        
        var resultSymbol = "";
        var alfabetSize = letters.Length;
        var gamma = IterationKey(password, text.Length);
        for (int i = 0; i < text.Length; i++)
        {
            var index = letters.IndexOf(text[i]);
            var codeIndex = letters.IndexOf(gamma[i]);

            if (index < 0)
            {
                resultSymbol += text[i].ToString();
            }
            else
            {
                resultSymbol += letters[(alfabetSize + index + ((encrypting ? 1 : -1) * codeIndex)) % alfabetSize].ToString();
            }
        }
        return resultSymbol;
    }

    private string IterationKey(string s, int n)
    {
        var p = s;
        while (p.Length < n)
        {
            p += p;
        }
        return p.Substring(0, n);
    }

    public string Encoder(string initMessage, string key) => Vigenere(initMessage, key);

    public string Decoder(string encodedMessage, string key) => Vigenere(encodedMessage, key, false);
}

class Program
{
    static void Main(string[] args)
    {
        var cipher = new CodeOfVigenere("ABCDEFGHIJKLMNOPQRSTUVWXYZ");
        string file_name = "initial.txt";
        var initText = File.ReadAllText(file_name, Encoding.UTF8);
        Console.WriteLine();
        Console.WriteLine("********************** Шифр Виженера **********************");
        Console.WriteLine();
        Console.WriteLine("----------------------------------------------------------");
        Console.WriteLine("Исходный текст: ");
        Console.WriteLine(initText);
        Console.WriteLine("----------------------------------------------------------");
        Console.Write("Введите ключ: ");
        var key = Console.ReadLine().ToUpper();
        Console.WriteLine("----------------------------------------------------------");
        var codedText = cipher.Encoder(initText, key);
        Console.WriteLine("####################### Результат #######################");
        Console.WriteLine();
        Console.WriteLine("Закодированный текст: {0}", codedText);
        Console.WriteLine("Декодированный текст: {0}", cipher.Decoder(codedText, key));
        Console.ReadLine();
    }
} 