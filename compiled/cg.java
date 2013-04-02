import processing.core.*; 
import processing.xml.*; 

import java.applet.*; 
import java.awt.Dimension; 
import java.awt.Frame; 
import java.awt.event.MouseEvent; 
import java.awt.event.KeyEvent; 
import java.awt.event.FocusEvent; 
import java.awt.Image; 
import java.io.*; 
import java.net.*; 
import java.text.*; 
import java.util.*; 
import java.util.zip.*; 
import java.util.regex.*; 

public class cg extends PApplet {

// TODO: Replace position ints with floats
// TODO: create a reset state
// TODO: Fix line blurring issue
// TODO: Fix frame rate issue
// TODO: Adjust the shrinkage proportion
// TODO: add a win screen
// TODO: add a red flash when you shrink
// TODO: add title screen with instructions, 
// TODO: change difficulty based on screen size
// TODO: color of circle is upward or downward trend

ArrayList companies = new ArrayList();

// myMan sizing
int es = 50;

//global variables for background color
int bgc = 30;

//gloabl variable for array of stocks and prices
String[] mCaps;

//gloabl win variable
boolean win = false;

// Timing variables
int startTime;
int counter = 0;
int once = 0;

public void setup() {
  size(800, 600);
  startTime = millis();
  mCaps = loadStrings("all.php");
  // println(mCaps);
  noCursor();
  strokeWeight(1);
}

public void draw() {
  stroke(255);
  scene();
  populateCompany();
  displayCompanies();
  myMan();
}

// Behavior and display for "myMan"
public void myMan() {
  fill(225, 100, 100);
  ellipse(mouseX, mouseY, es, es);
  fill(255);
  textSize(PApplet.parseInt(es/5));
  text(str(PApplet.parseInt(es))+"M", mouseX, mouseY);
  if(es >  width*1.5f) {win = true;}
  for (int i = companies.size()-1; i >= 0; i--) {
    Company c = (Company) companies.get(i);
    if (
      c.display().x >  mouseX - ((c.cSize()/2)+(es/2)) && 
      c.display().x <  mouseX + ((c.cSize()/2)+(es/2)) && 
      c.display().y >  mouseY - ((c.cSize()/2)+(es/2)) && 
      c.display().y <  mouseY + ((c.cSize()/2)+(es/2))
	  ) {

      // adjust size of myMan based on company size  
      if (es >  c.cSize()) {
        es+=(c.cSize()/2);
      } 

      if (es <  c.cSize()) {
        es-=(c.cSize()/8);
      }

      // if myMan is too small make 50
      if (es <  50) {
        es = 50;
      }
      companies.remove(i);
    }
  }
} 

public void scene() {
  background(bgc);
}

// draw company circles
public void populateCompany() {

  if (millis() >  startTime + 1000) {
    startTime = millis(); // reset start time
    counter ++; // add to the counter
    int companyPick = PApplet.parseInt((random(mCaps.length))/2);
    companies.add(new Company(companyPick));
    once = 0;
  }

  if (counter % 120 == 0 && once == 0) {
    mCaps = loadStrings("all.php");
    once = 1;
  }
}

// show and remove company circles
public void displayCompanies() {
  for (int i = companies.size()-1; i >= 0; i--) {
    Company c = (Company) companies.get(i);
    c.display();
  }

  for (int i = companies.size()-1; i >= 0; i--) {
    Company c = (Company) companies.get(i);
    if (c.display().y >  height) {
      companies.remove(i);
    }
  }
}

// a class for creating each company circle
class Company {
  int sValue;
  int ex;
  int ey;
  int ewh;
  PVector[] pairs;

  Company(int sv) {
    sValue = sv;
    ex = PApplet.parseInt(random(width));
    pairs = new PVector[PApplet.parseInt(mCaps.length/2)];
  }

  // outputs a PVector with x = to market value and y = to compnay stock symbol
  public PVector pairing(int n) {
    for (int i = 0; i <  pairs.length; i++) {
      pairs[i] = new PVector(i*2, (i*2)+1);
    }

    PVector p = new PVector(pairs[n].x, pairs[n].y);

    if (p.y >  width) { 
      p.y = width/1.5f;
    }
    if (p.y <  2) { 
      p.y = 2;
    }
    float m = map(p.y, 2, width/2, 2, width/2);
    p.y = PApplet.parseInt(m);

    return p;
  }

  public int mCap() {
    int x = mCaps[PApplet.parseInt(pairing(sValue).y)];
    return x;
  }

  public String symbol() {
    String x = mCaps[PApplet.parseInt(pairing(sValue).x)];
    return x;
  }

  public int cSize() {
    return ewh;
  }

  public PVector display() {
    ewh = mCap();
    ey++;
    fill(250);
    noFill();
    ellipse(ex, ey, ewh, ewh);
    fill(225, 100, 100);
    int ts = PApplet.parseInt(ewh/5);
    if (ts <  10) {
      ts = 15;
    }
    textSize(ts);
    textAlign(CENTER);
    text(symbol(), ex, ey);
    PVector CompanyPosition = new PVector(ex, ey);
    return CompanyPosition;
  }
}

  static public void main(String args[]) {
    PApplet.main(new String[] { "--bgcolor=#FFFFFF", "cg" });
  }
}
