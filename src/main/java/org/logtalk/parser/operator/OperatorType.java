package org.logtalk.parser.operator;

import static org.logtalk.parser.operator.OperatorPosition.BINARY;
import static org.logtalk.parser.operator.OperatorPosition.LEFT;
import static org.logtalk.parser.operator.OperatorPosition.RIGHT;

import java.util.Optional;

public enum OperatorType {
   FX, FY, XF, YF, XFX, XFY, YFX;

   private final OperatorPosition position;
   private final Optional<PrecedenceType> leftArgumentPrecedence;
   private final Optional<PrecedenceType> rightArgumentPrecedence;

   OperatorType() {
      if (name().length() == 3) {
         position = BINARY;
      } else if (name().charAt(0) == 'F') {
         position = LEFT;
      } else {
         position = RIGHT;
      }
      if (!isLeft()) {
         leftArgumentPrecedence = Optional.of(PrecedenceType.of(name().charAt(0)));
      } else {
         leftArgumentPrecedence = Optional.empty();
      }
      if (!isRight()) {
         rightArgumentPrecedence = Optional.of(PrecedenceType.of(name().charAt(name().length() - 1)));
      } else {
         rightArgumentPrecedence = Optional.empty();
      }
   }

   public boolean isBinary() {
      return position == BINARY;
   }

   public boolean isLeft() {
      return position == LEFT;
   }

   public boolean isRight() {
      return position == RIGHT;
   }

   public OperatorPosition getPosition() {
      return position;
   }

   public PrecedenceType getLeftArgumentPrecedence() {
      return leftArgumentPrecedence.get();
   }

   public PrecedenceType getRightArgumentPrecedence() {
      return rightArgumentPrecedence.get();
   }

   public boolean requiresLeftArgument() {
      return isBinary() || isRight();
   }

   public boolean requiresRightArgument() {
      return isBinary() || isLeft();
   }

   @Override
   public String toString() {
      return super.toString().toLowerCase();
   }
}
