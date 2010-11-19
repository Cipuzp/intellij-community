package com.jetbrains.python.inspections;

import com.intellij.codeInspection.ProblemHighlightType;
import com.intellij.codeInspection.ProblemsHolder;
import com.intellij.psi.PsiElementVisitor;
import com.intellij.psi.PsiWhiteSpace;
import com.jetbrains.python.PyBundle;
import com.jetbrains.python.actions.RedundantParenthesesQuickFix;
import com.jetbrains.python.psi.*;
import org.jetbrains.annotations.Nls;
import org.jetbrains.annotations.NotNull;

/**
 * User: catherine
 *
 * Inspection to detect redundant parentheses in if/while statement.
 */
public class PyRedundantParenthesesInspection extends PyInspection {
  @Nls
  @NotNull
  @Override
  public String getDisplayName() {
    return PyBundle.message("INSP.NAME.redundant.parentheses");
  }

  @NotNull
  @Override
  public PsiElementVisitor buildVisitor(@NotNull ProblemsHolder holder, boolean isOnTheFly) {
    return new Visitor(holder);
  }

  private static class Visitor extends PyInspectionVisitor {

    public Visitor(final ProblemsHolder holder) {
      super(holder);
    }

    @Override
    public void visitPyParenthesizedExpression(final PyParenthesizedExpression node) {
      if (node.getContainedExpression() instanceof PyReferenceExpression ||
            node.getContainedExpression() instanceof PyStringLiteralExpression
              || node.getContainedExpression() instanceof PyNumericLiteralExpression) {
        registerProblem(node, "Remove redundant parentheses", ProblemHighlightType.INFORMATION, null, new RedundantParenthesesQuickFix());
      }
      else if (node.getParent() instanceof PyIfPart || node.getParent() instanceof PyWhilePart
                  || node.getParent() instanceof PyReturnStatement) {
        if (!node.getText().contains("\n")) {
          registerProblem(node, "Remove redundant parentheses", ProblemHighlightType.INFORMATION, null, new RedundantParenthesesQuickFix());
        }
      }
    }

  }
}
