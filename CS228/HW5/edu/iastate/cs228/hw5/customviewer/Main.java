package edu.iastate.cs228.hw5.customviewer;

import javax.swing.SwingUtilities;

import edu.iastate.cs228.hw5.PathFinderFactoryImpl;

public class Main
{
  public static void main(String[] args)
  {
    Runnable r = new Runnable()
    {
      public void run()
      {
        GraphAnimationViewerCustom.createAndShow(new PathFinderFactoryImpl());
      }
    };
    SwingUtilities.invokeLater(r);
    System.out.println("Main thread exiting");
  }
}
