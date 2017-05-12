/**
 * Created by user on 09/05/2017.
 */


    import java.awt.*;
            import java.awt.event.*;


public class MyCalculator extends Frame
{

    final int FRAME_WIDTH=400,FRAME_HEIGHT=400;
    final int HEIGHT=40, WIDTH=40, H_SPACE=25,V_SPACE=15;
    final int TOPX=50, TOPY=50;
    public boolean setClear=true;
    double number, memValue;
    char op;
    Label displayLabel=new Label("0",Label.LEFT);
    Label memLabel=new Label(" ",Label.LEFT);
    String digitButtonText[] = {"1", "2", "3", "4", "5", "6", "7", "8", "9", "0","." };
    String operatorButtonText[] = {"+", "-", "*", "/", "1/X", "=" };
    String specialButtonText[] = {"Backspace", "AC","+/-" };
    MyDigitButton digitButton[]=new MyDigitButton[digitButtonText.length];
    MyOperatorButton operatorButton[]=new MyOperatorButton[operatorButtonText.length];
    MySpecialButton specialButton[]=new MySpecialButton[specialButtonText.length];



//Kontraktor
    MyCalculator(String frameText)
    {
        super(frameText);

        int tempA=TOPX, b=TOPY;
        displayLabel.setBounds(tempA,b,300,HEIGHT);
        displayLabel.setBackground(Color.PINK);
        displayLabel.setForeground(Color.BLACK);
        add(displayLabel);

        //set Co-ordinates for Digit Buttons
        int digitA=TOPX;
        int digitB=TOPY+2*(HEIGHT+V_SPACE);
        tempA=digitA;  b=digitB;
        for(int i=0;i<digitButton.length;i++)
        {
            digitButton[i]=new MyDigitButton(tempA,b,WIDTH,HEIGHT,digitButtonText[i], this);
            digitButton[i].setForeground(Color.BLUE);
            tempA+=WIDTH+H_SPACE;
            if((i+1)%3==0){tempA=digitA; b+=HEIGHT+V_SPACE;}
        }

        //spesial buttons

        tempA=TOPX+1; b=TOPY+1*(HEIGHT+V_SPACE);
        for(int i=0;i<specialButton.length;i++)
        {
            specialButton[i]=new MySpecialButton(tempA,b,WIDTH*3,HEIGHT,specialButtonText[i], this);
            specialButton[i].setForeground(Color.BLUE);
            tempA=tempA+2*WIDTH+H_SPACE;
        }


        //set Co-ordinates for Operator Buttons
        int opsX=digitA+2*(WIDTH+H_SPACE)+H_SPACE;
        int opsY=digitB;
        tempA=opsX;  b=opsY;
        for(int i=0;i<operatorButton.length;i++)
        {
            tempA+=WIDTH+H_SPACE;
            operatorButton[i]=new MyOperatorButton(tempA,b,WIDTH,HEIGHT,operatorButtonText[i], this);
            operatorButton[i].setForeground(Color.BLUE);
            if((i+1)%2==0){tempA=opsX; b+=HEIGHT+V_SPACE;}
        }

        addWindowListener(new WindowAdapter()
        {
            public void windowClosing(WindowEvent ev)
            {System.exit(0);}
        });

        setLayout(null);
        setSize(FRAME_WIDTH,FRAME_HEIGHT);
        setVisible(true);
    }

    static String getFormattedText(double temp)
    {
        String resText=""+temp;
        if(resText.lastIndexOf(".0")>0)
            resText=resText.substring(0,resText.length()-2);
        return resText;
    }

    public static void main(String []args)
    {
        new MyCalculator("Kalkulator");
    }
}

class MyOperatorButton extends Button implements ActionListener
{
    MyCalculator cl;

    MyOperatorButton(int x,int y, int width,int height,String cap, MyCalculator clc)
    {
        super(cap);
        setBounds(x,y,width,height);
        this.cl=clc;
        this.cl.add(this);
        addActionListener(this);
    }

    public void actionPerformed(ActionEvent ev)
    {
        String opText=((MyOperatorButton)ev.getSource()).getLabel();

        cl.setClear=true;
        double temp=Double.parseDouble(cl.displayLabel.getText());

        if(opText.equals("1/x"))
        {
            try
            {double tempd=1/(double)temp;
                cl.displayLabel.setText(MyCalculator.getFormattedText(tempd));}
            catch(ArithmeticException excp)
            {cl.displayLabel.setText("Divide by 0.");}
            return;
        }

        if(!opText.equals("="))
        {
            cl.number=temp;
            cl.op=opText.charAt(0);
            return;
        }
        // process = button pressed
        switch(cl.op)
        {
            case '+':
                temp+=cl.number;break;
            case '-':
                temp=cl.number-temp;break;
            case '*':
                temp*=cl.number;break;
            case '/':
                try{temp=cl.number/temp;}
                catch(ArithmeticException excp)
                {cl.displayLabel.setText("Divide by 0."); return;}
                break;
        }

        cl.displayLabel.setText(MyCalculator.getFormattedText(temp));

    }//actionPerformed
}//class



class MyDigitButton extends Button implements ActionListener
{
    MyCalculator cl;

    MyDigitButton(int x,int y, int width,int height,String cap, MyCalculator clc)
    {
        super(cap);
        setBounds(x,y,width,height);
        this.cl=clc;
        this.cl.add(this);
        addActionListener(this);
    }

    static boolean isInString(String s, char ch)
    {
        for(int i=0; i<s.length();i++) if(s.charAt(i)==ch) return true;
        return false;
    }

    public void actionPerformed(ActionEvent ev)
    {
        String tempText=((MyDigitButton)ev.getSource()).getLabel();

        int index=0;
        try{
            index=Integer.parseInt(tempText);
        }catch(NumberFormatException e){return;}

        if (index==0 && cl.displayLabel.getText().equals("0")) return;

        if(cl.setClear)
        {cl.displayLabel.setText(""+index);cl.setClear=false;}
        else
            cl.displayLabel.setText(cl.displayLabel.getText()+index);


        if(tempText.equals("."))
        {
            if(cl.setClear)
            {cl.displayLabel.setText("0.");cl.setClear=false;}
            else if(!isInString(cl.displayLabel.getText(),'.'))
                cl.displayLabel.setText(cl.displayLabel.getText()+".");
            return;
        }


    }//actionPerformed
}//class defination


class MySpecialButton extends Button implements ActionListener
{
    MyCalculator cl;

    MySpecialButton(int x,int y, int width,int height,String cap, MyCalculator clc)
    {
        super(cap);
        setBounds(x,y,width,height);
        this.cl=clc;
        this.cl.add(this);
        addActionListener(this);
    }

    static String backSpace(String s)
    {
        String Res="";
        for(int i=0; i<s.length()-1; i++) Res+=s.charAt(i);
        return Res;
    }


    public void actionPerformed(ActionEvent ev)
    {
        String opText=((MySpecialButton)ev.getSource()).getLabel();
        //check for backspace button

        if(opText.equals("Backspace"))
        {
            String tempText=backSpace(cl.displayLabel.getText());
            if(tempText.equals(""))
                cl.displayLabel.setText("0");
            else
                cl.displayLabel.setText(tempText);
            return;
        }

        if(opText.equals("C"))
        {
            cl.number=0.0; cl.op=' '; cl.memValue=0.0;
            cl.memLabel.setText(" ");
        }


        cl.displayLabel.setText("0");cl.setClear=true;
    }//actionPerformed
}//class


