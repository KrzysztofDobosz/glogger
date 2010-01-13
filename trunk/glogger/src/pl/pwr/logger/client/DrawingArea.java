package pl.pwr.logger.client;

import java.util.ArrayList;

import com.google.gwt.user.client.ui.MouseListener;
import com.google.gwt.user.client.ui.Widget;

import gwt.canvas.client.Canvas;

public class DrawingArea extends Canvas
{

	private ArrayList<Double> sx = new ArrayList<Double>();
	private ArrayList<Double> sy = new ArrayList<Double>();
	
	public DrawingArea()
	{
		super(300, 300);
		setBackgroundColor("black");
		setFillStyle("white");
		fillRect(2, 2, 296, 296); 
		addMouseListener(new MouseListener() {
			
			boolean p;
			int oldx, oldy;
			@Override
			public void onMouseUp(Widget sender, int x, int y) {
				p = false;
				closePath();
				
			}	
			@Override
			public void onMouseMove(Widget sender, int x, int y) {
				
				if (p)
				{
					lineTo(x, y);
					stroke();
					sx.add((double)x);
					sy.add((double)y);
				}
			}
			@Override
			public void onMouseLeave(Widget sender) {
				p = false;
				closePath();
			}
			@Override
			public void onMouseEnter(Widget sender) {
				// TODO Auto-generated method stub
				
			}
			@Override
			public void onMouseDown(Widget sender, int x, int y) {
				setFillStyle("black");
				beginPath();
				p = true;
				
			}
		});

	}
	
	public String getData()
	   {
		   double[] x = new double[51];
		   double[] y = new double[51];
		   
		   double[] angles = new double[50]; 
		   
		   for(int i = 0; i<51; i++)
		   {
			   x[i] = sx.get(i*sx.size()/51);
			   y[i] = sy.get(i*sx.size()/51);
			   setFillStyle("red");
			   fillRect(x[i], y[i], 2, 2);
			   setFillStyle("black");
		   }
		   for(int i = 1; i<51; i++)
		   {
			   if ((x[i] > x[i-1]) && (y[i] >= y[i-1]))
	               angles[i-1] = (Math.atan((x[i] - x[i-1])/(y[i] - x[i-1])));
			   else if ((x[i] > x[i-1]) && (y[i] < y[i-1]))
	               angles[i-1] = (2*Math.PI + Math.atan((y[i] - y[i-1])/(x[i] - x[i-1])));
			   else if((x[i] < x[i-1]) && (y[i] >= y[i-1]))
	               angles[i-1] = (Math.PI + Math.atan((y[i] - y[i-1])/(x[i] - x[i-1])));
			   else if((x[i] < x[i-1]) && (y[i] < y[i-1]))
	               angles[i-1] = (Math.PI + Math.atan((y[i] - y[i-1])/(x[i] - x[i-1])));
			   else if((x[i] == x[i-1]) && (y[i] < y[i-1]))
	               angles[i-1] = (1.5*Math.PI);
			   else if((x[i] == x[i-1]) && (y[i] >= y[i-1]))
	               angles[i-1] = (Math.PI/2);
		   }
		   String result = "";
		   for(double d : angles)
			   result += d + ",";
		   return result;
	   }
	public void clear()
	{
		setFillStyle("white");
		fillRect(2, 2, 296, 296);
		sx.clear();
		sy.clear();
	}
	
}
