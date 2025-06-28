package org.bx.idgenerator.bean;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * @author zero
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ApiModel(value = "基础返回实体")
public class ResultWrapper<T> implements Serializable {

	/**
	 * 响应提示信息
	 */
	@ApiModelProperty(name = "msg", dataType = "string", value = "响应信息")
	private String msg;
	/**
	 * 返回码：200正常，500以上为错误信息
	 */
	@ApiModelProperty(name = "code", dataType = "int", value = "响应码")
	private int code;
	/**
	 * 返回
	 */
	@ApiModelProperty(name = "data", dataType = "object", value = "数据内容")
	private T data;

	public static ResultWrapper.ResultWrapperBuilder SUCCESS_BUILDER() {
		return builder().code(StateCode.SUCCESS.code()).msg(StateCode.SUCCESS.msg());
	}

	public static ResultWrapper.ResultWrapperBuilder ERROR_BUILDER() {
		return builder().code(StateCode.ERROR.code()).msg(StateCode.ERROR.msg());
	}


	/**
	 * 建议使用 {@link ResultWrapper#getSuccessResultWrapper(Object o)}, 带泛型的,可以检查出类型不一致的情况
	 * @return
	 */
	@Deprecated
	public static ResultWrapperBuilder getSuccessBuilder(){
		return  ResultWrapper.builder().code(StateCode.SUCCESS.code()).msg(StateCode.SUCCESS.msg());
	}

	/**
	 * 建议使用 {@link ResultWrapper#getErrorResultWrapper(Object o)} , 带泛型的,可以检查出类型不一致的情况
	 * @return
	 */
	@Deprecated
	public static ResultWrapperBuilder getErrorBuilder(){
		return  ResultWrapper.builder().code(StateCode.ERROR.code()).msg(StateCode.SUCCESS.msg());
	}


	/**
	 * 获取默认的成功信息,
	 * 默认的成功状态码 {@link StateCode#SUCCESS}
	 * @param data
	 * @param <T>
	 * @return
	 */
	public static <T> ResultWrapper<T> getSuccessResultWrapper(T data) {
		return ResultWrapper.<T>builder().code(StateCode.SUCCESS.code()).msg(StateCode.SUCCESS.msg()).data(data).build();
	}

	/**
	 * 获取默认的错误的信息
	 * 默认的失败状态码 {@link StateCode#ERROR}
	 * @param data
	 * @param <T>
	 * @return
	 */
	public static <T> ResultWrapper<T> getErrorResultWrapper(T data) {
		return ResultWrapper.<T>builder().code(StateCode.ERROR.code()).data(data).build();
	}


	/**
	 * 获取约定好的自定义的成功信息，方便前端业务特殊处理
	 * @param data 业务数据
	 * @param stateCode 状态码信息
	 * @param <T>
	 * @return
	 */
	public static <T> ResultWrapper<T> getCustomResultWrapper(T data,StateCode stateCode) {
		return ResultWrapper.<T>builder().code(stateCode.code()).msg(stateCode.msg()).data(data).build();
	}

	/**
	 * 获取约定好的自定义的成功信息，方便前端业务特殊处理
	 * 默认的成功状态码 {@link StateCode#SUCCESS}
	 * @param data 业务数据
	 * @param code 状态码
	 * @param msg 提示信息
	 * @param <T>
	 * @return
	 */
	public static <T> ResultWrapper<T> getCustomResultWrapper(T data, int code, String msg) {
		return ResultWrapper.<T>builder().code(code).msg(msg).data(data).build();
	}

}
