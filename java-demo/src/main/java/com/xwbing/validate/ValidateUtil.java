package com.xwbing.validate;

import com.xwbing.util.CommonEnum;
import org.apache.commons.lang.StringUtils;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Map;
/**
 * 
 * 说明: 校验工具类，用于校验保存入参
 * 项目名称: zdemo
 * 创建日期: 2016年12月19日 下午3:46:41
 * 作者: xiangwb
 */
public class ValidateUtil
{
	/**
	 * 校验Vo
	 * 
	 * @param obj
	 *            页面Vo
	 * @param regMap
	 *            页面vo对应的正则配置MAP
	 * @return
	 */
	public static ValidateResult validate(Object obj, Map<String, String> regMap)
	{
		ValidateResult vr = new ValidateResult();

		Class<? extends Object> classVO = obj.getClass();

		Field[] declaredFields = classVO.getDeclaredFields();

		for (Field field : declaredFields)
		{
			String fieldName = field.getName();
			String reg;
			StringBuffer getMethodStr;
			// 拼接get方法
			getMethodStr = new StringBuffer();
			getMethodStr.append("get");
			getMethodStr.append(fieldName.substring(0, 1).toUpperCase());
			getMethodStr.append(fieldName.substring(1));
			String value;
			Method getMethod;
			try
			{
				getMethod = classVO.getMethod(getMethodStr.toString());

			}
			catch (NoSuchMethodException e1)
			{
				System.out.println("NoSuchMethodException");
				continue;
			}
			catch (SecurityException e1)
			{
				System.out.println("SecurityException");
				continue;
			}

			reg = regMap.get(fieldName);
			// 如果不配置校验则不校验
			if (StringUtils.isEmpty(reg))
			{
				continue;
			}
			// 执行get方法
			try
			{
				value = String.valueOf(getMethod.invoke(obj, new Object[0]));
				
				value = "null" == value?null :value;
				
				vr = validateValueWithReg(value, reg, fieldName);

				if (!vr.isSuccess())
				{
					return vr;
				}
			}
			catch (IllegalAccessException e)
			{
				System.out.println("IllegalAccessException:");
				vr.setErrorCode(CommonEnum.ValidateResultEnum.FAILED);
				return vr;
			}
			catch (IllegalArgumentException e)
			{
				System.out.println("IllegalArgumentException:");
				vr.setErrorCode(CommonEnum.ValidateResultEnum.FAILED);
				return vr;
			}
			catch (InvocationTargetException e)
			{
				System.out.println("InvocationTargetException:");
				vr.setErrorCode(CommonEnum.ValidateResultEnum.FAILED);
				return vr;
			}

		}

		return vr;
	}

	/**
	 * 正则校验
	 * 
	 * @param value
	 * @param reg
	 *            校验规则
	 * @return
	 */
	private static ValidateResult validateValueWithReg(String value,
			String reg, String fieldName)
	{
		ValidateResult vr = new ValidateResult();
		String[] split = reg.split(";");
		if (null != split && split.length >= 1)
		{
			// 校验规则用；隔开，前面表示是否必填，后面表示正则校验
			String required = split[0];
			if (StringUtils.isEmpty(required))
			{
				System.out.println("配置文件中没有配置" + fieldName + "的必填校验");
			}
			else
			{
				// 如果是必填校验
				if ("true".equalsIgnoreCase(required))
				{
					if (StringUtils.isEmpty(value))
					{
						vr.setErrorCode(CommonEnum.ValidateResultEnum.FAILED);
						vr.setErrorMsg(fieldName + "不能为空");
						return vr;
					}
				}
				else
				{
					// 如果非必填，为空则返回true
					if (StringUtils.isEmpty(value))
					{
						return vr;
					}
				}
				if (split.length > 1)
				{
					String regexStr = split[1];
					if (StringUtils.isEmpty(regexStr))
					{
						System.out.println("配置文件中没有配置" + fieldName + "的校验");
						return vr;
					}
					String[] regexSplit;
					String errorMsg;
					String regex;
					//除去第一项必填项，剩下的正则校验
					for (int j = 1; j < split.length; j++)
					{
						regexStr = split[j];

						regexSplit = regexStr.split("#");

						// 如果没有配置校验
						if (null == regexSplit || regexSplit.length < 1)
						{
							vr.setErrorCode(CommonEnum.ValidateResultEnum.FAILED);
							vr.setErrorMsg(fieldName + "的正则未正确配置");
							return vr;
						}
						// 如果没有配置正则的错误提示
						if (regexSplit.length == 1)
						{
							errorMsg = CommonEnum.ValidateResultEnum.FAILED
									.getMsg();
							System.out.println(fieldName + "的校验提示语未设置");
						}
						else
						{
							// 错误提示
							errorMsg = regexSplit[1];
						}
						// 正则校验
						regex = regexSplit[0];
						// 校验
						boolean matches = value.matches(regex);
						// 返回结果
						if (!matches)
						{
							vr.setErrorCode(CommonEnum.ValidateResultEnum.FAILED);
							vr.setErrorMsg(errorMsg);
							return vr;
						}
						else
						{
							continue;
						}
					}
				}
			}
		}
		else
		{
			vr.setErrorCode(CommonEnum.ValidateResultEnum.FAILED);
			vr.setErrorMsg("参数：" + value + "校验配置错误!");
			System.out.println("校验配置错误！");
		}
		return vr;
	}
	
	/**
	 * 后台身份证校验
	 * @param idCardStr
	 * @return 非必填项，如果为空返回true
	 */
	public static boolean checkIdCard(String idCardStr)
	{
		String re = "^\\d{17}(\\d|X|x)";

		//身份证可以不填
		if (StringUtils.isEmpty(idCardStr))
		{
			return true;
		}
		if (!idCardStr.matches(re))
		{
			return false;
		}

		int[] weight = new int[] { 7, 9, 10, 5, 8, 4, 2, 1, 6, 3, 7, 9, 10, 5,
				8, 4, 2 }; // 十七位数字本体码权重
		char[] validate = new char[] { '1', '0', 'X', '9', '8', '7', '6', '5',
				'4', '3', '2' }; // mod11,对应校验码字符值
		int sum = 0;
		int mode = 0;
		char[] id17 = idCardStr.toCharArray();
		for (int i = 0; i < 17; i++)
		{
			sum += (Character.getNumericValue(id17[i]) * weight[i]);
		}
		mode = sum % 11;

		char c = id17[17];
		return c == validate[mode];
	}

}
