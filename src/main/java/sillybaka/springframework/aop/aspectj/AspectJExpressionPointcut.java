package sillybaka.springframework.aop.aspectj;

import org.aspectj.weaver.tools.PointcutExpression;
import org.aspectj.weaver.tools.PointcutParser;
import org.aspectj.weaver.tools.PointcutPrimitive;
import sillybaka.springframework.aop.ClassFilter;
import sillybaka.springframework.aop.MethodMatcher;
import sillybaka.springframework.aop.support.ExpressionPointcut;

import java.lang.reflect.Method;
import java.util.HashSet;
import java.util.Set;

/**
 * 基于AspectJ提供的接口的表达式切入点，使用AspectJ表达式（当前只支持execution表达式）
 * <p>Date: 2022/10/27
 * <p>Time: 21:23
 *
 * @Author SillyBaka
 **/
public class AspectJExpressionPointcut implements ExpressionPointcut,ClassFilter,MethodMatcher {
    private static final Set<PointcutPrimitive> SUPPORTED_PRIMITIVES = new HashSet<>();

    static {
        SUPPORTED_PRIMITIVES.add(PointcutPrimitive.EXECUTION);
    }

    private final PointcutExpression pointcutExpression;

    public AspectJExpressionPointcut(String expression) {
        PointcutParser pointcutParser = PointcutParser.getPointcutParserSupportingSpecifiedPrimitivesAndUsingSpecifiedClassLoaderForResolution(SUPPORTED_PRIMITIVES, this.getClass().getClassLoader());
        pointcutExpression = pointcutParser.parsePointcutExpression(expression);
    }

    @Override
    public boolean matches(Class<?> clazz) {
        return pointcutExpression.couldMatchJoinPointsInType(clazz);
    }

    @Override
    public boolean matches(Method method, Class<?> targetClass) {
        return pointcutExpression.matchesMethodExecution(method).alwaysMatches();
    }

    @Override
    public ClassFilter getClassFilter() {
        return this;
    }

    @Override
    public MethodMatcher getMethodMatcher() {
        return this;
    }

    @Override
    public String getExpression() {
        return pointcutExpression.getPointcutExpression();
    }
}
