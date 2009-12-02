package pl.pwr.logger.server;

import java.lang.Math;
import Jama.Matrix;

/**
 * Two-layer NN with backpropagation learning algorithm 
 * @author krzycho
 *
 */
public class NeuralNetwork
{
   private double beta;
   private double eta;
   private Matrix alphaMatrix;
   private Matrix betaMatrix;
   private Matrix v, y;
   
   /**
    * Constuctor
    * @param alphaM weight parameters for first layer
    * @param betaM weight parameters for second layer
    * @param inBeta factor for evaluation function
    * @param inEta learning step
    */
   public NeuralNetwork(Matrix alphaM, Matrix betaM,
         double inBeta, double inEta)
   {
      alphaMatrix = alphaM;
      betaMatrix = betaM;
      beta = inBeta;
      eta = inEta;
      //v = new Matrix(1,1);
      //y = new Matrix(1,1);
   }
   /**
    * Activation function, in this case - logistic
    * @param x
    * @return
    */
   private Double activationFunction(Double x)
   {
      return 1 / (1 + Math.exp(-beta * x));
     // return x;
   }
   
   /**
    * Calculates output of the network and saves partial results
    * @param inputVector  
    * @return
    */
   public Matrix getOutput(Matrix inputVector)
   {
      v = alphaMatrix.times(inputVector);
      for (int i = 0; i < v.getRowDimension(); i++)
      {
         for (int j = 0; j < v.getColumnDimension(); j++)
         {
            v.set(i, j, activationFunction(v.get(i, j)));
         }
      }
      y = betaMatrix.times(v);
      for (int i = 0; i < y.getRowDimension(); i++)
      {
         for (int j = 0; j < y.getColumnDimension(); j++)
         {
            y.set(i, j, activationFunction(y.get(i, j)));
         }
      }
      return y;
   }
   /**
    * Backpropagation learning for NN
    * @param inputVector
    * @param outputVector
    */
   public void learn(Matrix inputVector, Matrix outputVector)
   {
      Matrix delta = outputVector.minus(getOutput(inputVector));
      Matrix epsilon = new Matrix(alphaMatrix.getRowDimension(),1);

      for (int m = 0; m < alphaMatrix.getRowDimension(); m ++)
      {
         double temp = 0.;
         for(int n = 0; n < betaMatrix.getRowDimension(); n ++)
         {
            temp += betaMatrix.get(n, m) * delta.get(n,0);
         }
         epsilon.set(m,0,temp);
      }

      for (int m = 0; m < betaMatrix.getColumnDimension(); m ++)
         for(int n = 0; n < betaMatrix.getRowDimension(); n ++)
         {
            double temp = betaMatrix.get(n, m);
            temp += eta * delta.get(n,0) * y.get(n,0)*(1-y.get(n,0)) * v.get(m,0);
            betaMatrix.set(n, m, temp);
         }
      for (int m = 0; m < alphaMatrix.getColumnDimension(); m ++)
         for(int n = 0; n < alphaMatrix.getRowDimension(); n ++)
         {
            double temp = alphaMatrix.get(n, m);
            temp += eta * epsilon.get(n,0) * v.get(n,0) * (1-v.get(n,0)) *
               inputVector.get(m,0);
            alphaMatrix.set(n, m, temp);
         }
      
   }
}
