package org.bsshare.tv.common.validator;

import org.bsshare.tv.model.entity.User;
import org.bsshare.tv.model.front.web.SignUpUser;
import org.bsshare.tv.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

@Component
public class UserValidator implements Validator {

	@Autowired
	private UserService userService;

	@Override
	public boolean supports(Class<?> aClass) {
		return User.class.equals(aClass);
	}

	@Override
	public void validate(Object o, Errors errors) {
		SignUpUser user = (SignUpUser) o;

		ValidationUtils.rejectIfEmptyOrWhitespace(errors, "username", "validation.notempty");
		if (user.getUsername().length() < 6 || user.getUsername().length() > 32) {
			errors.rejectValue("username", "validation.size.userform.username");
		}
		if (userService.findByUsername(user.getUsername()) != null) {
			errors.rejectValue("username", "validation.duplicate.userform.username");
		}

		ValidationUtils.rejectIfEmptyOrWhitespace(errors, "password", "validation.notempty");
		if (user.getPassword().length() < 8 || user.getPassword().length() > 32) {
			errors.rejectValue("password", "validation.size.userform.password");
		}

		if (!user.getPasswordConfirm().equals(user.getPassword())) {
			errors.rejectValue("passwordConfirm", "validation.diff.userform.passwordconfirm");
		}
	}
}
