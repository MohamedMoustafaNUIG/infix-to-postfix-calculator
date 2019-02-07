import javax.swing.JOptionPane;
public class InfixToPostfixCalc
{

    private ArrayStack list;
    private String input;//string input by user (infix)
    private int flag;
    private String output;//string to be output (postfix)
    private double answer;//value of input expression
    public InfixToPostfixCalc()
    {
        list=new ArrayStack(20);
        input=new String();
        output=new String();
        answer=0;
        enterString();
        flag=0;//will be used to check for invalid input and display appropriate error message
        char[] input_2=input.toCharArray();//Turns input string into a char array
        output=(String)toPostfix(input_2);//Method responsible for converting from infix to postfix
        answer=evaluate(output);
        JOptionPane.showMessageDialog(null,"The result of the expression is:\nInfix: "+input+"\n"+"Postfix: "+output+"\n"+"Result: "+answer);
    }

    public void setInput(String input)
    {
        this.input=input;
    }

    public String getInput()
    {
        return input;
    }

    public void enterString()
    {
        do {
            input=JOptionPane.showInputDialog("Please enter an infix numerical expression \n Must be between 3 and 20 characters");
            flag=verifyString(input);
            switch(flag)
            {
                case 1: JOptionPane.showMessageDialog(null,"Expression is too short (must be 3-20 in length) !");break;
                case 2: JOptionPane.showMessageDialog(null,"Expression is too long (must be 3-20 in length) !");break;
                case 3: JOptionPane.showMessageDialog(null,"Expression contains invalid characters!\nAllowed characters:+,-,/,*,^,and numbers 0-9");break;
                case 4: JOptionPane.showMessageDialog(null,"Expression contains multi-digit integers!");break;
            }
            }while(flag!=0);

    }

    public int verifyString(String input)
    {
        int flag=0;
        /*
        too small=>flag=1
        too large=>flag=2
        invalid characters=>flag=3
        double digits=>flag=4
        */
        if(input.length()<3){flag=1;}
        else if(input.length()>20){flag=2;}
        else {
            for (int i = 0; i < input.length(); i++) {
                /*
                ASCII values for accepted characters will be used to check validity of expression
                40-43:  (,),*,+
                45:     -
                47-57:  /,0-9
                94:     ^
                */
                if ((input.charAt(i)>=40&&input.charAt(i)<=43)||input.charAt(i)==45||(input.charAt(i)>=47&&input.charAt(i)<=57)||input.charAt(i)==94)
                {

                }else
                    {
                        flag=3;
                    }

                    if((i<input.length()-1)&&(input.charAt(i)>=48&&input.charAt(i)<=57)&&(input.charAt(i+1)>=48&&input.charAt(i+1)<=57)){flag=4;}

            }
        }
        return flag;
    }

    public int precedence(char operator)
    {
        switch(operator)
        {
            case '^': return 3;
            case '*':
            case '/': return 2;
            case '+':
            case '-': return 1;
        }
        JOptionPane.showMessageDialog(null,"Invalid operator input !!!");
        return 0;
    }

    public String toPostfix(char input[])
    {
        int k=0;
        char[] charArrayTemp=new char[20];
        for(int i=0;i<input.length;i++)
        {
            if(input[i]>=48&&input[i]<=57)//If character is a digit
            {
                charArrayTemp[k]=input[i];
                k++;
            }else if(input[i]==40)//If character is an opening bracket
            {
                list.push(input[i]);
            }else if(input[i]==41)//If character is a closing bracket
            {
                while((char)list.top()!=40)//Loops till the first open bracket, but doesnt pop it in loop
                {
                    charArrayTemp[k]=(char)list.pop();
                    k++;
                }
                list.pop();//pops opening bracket, gets rid of it
            }else
                {

                    if((list.isEmpty()==true) || ((char)list.top()==40))
                    {
                        list.push(input[i]);
                    }else if((precedence(input[i])>precedence((char)list.top())))
                    {
                        list.push(input[i]);
                    }else
                        {
                            if(list.isEmpty()!=true) {
                                while ((char) list.top() != 40 || precedence((char) list.top()) >= precedence(input[i])) {
                                    charArrayTemp[k] = (char) list.pop();
                                    k++;
                                    if (list.isEmpty() == true) {
                                        break;
                                    }//to make sure not to go over boundary
                                }
                            }
                            list.push(input[i]);
                        }
                }
        }

        while(list.isEmpty()!=true)
        {
            charArrayTemp[k]=(char)list.pop();
            k++;
        }
        String str = new String(charArrayTemp);
        return str;
    }

    public double evaluate(String input)
    {
        char[] expression = input.toCharArray();//makes iteration easier
        ArrayStack operands = new ArrayStack();
        double[] evalOperators = new double[2];
        double ans=0.0;
        for(int i=0;expression[i]!=0;i++)
        {
            if(expression[i]>=48 && expression[i]<=57)
            {
                operands.push(expression[i]);
            }else
                {
                    //Reduces code and makes evaluating operations easier
                    evalOperators[1]=Double.parseDouble(operands.pop().toString());
                    evalOperators[0]=Double.parseDouble(operands.pop().toString());
                    switch(expression[i])
                    {
                        case '+': ans = evalOperators[0]+evalOperators[1]; break;
                        case '-': ans = evalOperators[0]-evalOperators[1]; break;
                        case '*': ans = evalOperators[0]*evalOperators[1]; break;
                        case '/': ans = evalOperators[0]*1.0/evalOperators[1]; break;
                        case '^': ans = Math.pow(evalOperators[0],evalOperators[1]); break;
                    }
                    operands.push((Object)ans);
                }
        }
        return ans;
    }
}
