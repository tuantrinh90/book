package com.dz.customizes.views.edittexts

import android.annotation.SuppressLint
import android.content.Context
import android.text.Editable
import android.text.InputFilter
import android.text.TextWatcher
import android.text.method.PasswordTransformationMethod
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.view.inputmethod.EditorInfo
import android.widget.ImageView
import android.widget.LinearLayout
import androidx.core.content.ContextCompat
import butterknife.BindView
import butterknife.ButterKnife
import com.daimajia.androidanimations.library.Techniques
import com.daimajia.androidanimations.library.YoYo
import com.dz.libraries.utilities.*
import com.dz.libraries.views.edittexts.ExtEditText
import com.dz.libraries.views.textviews.ExtTextView
import com.dz.ui.R
import com.dz.utilities.AppUtility

class EditTextApp : LinearLayout {
    companion object {
        var DURATION = 650L
    }

    @BindView(R.id.llView)
    lateinit var llView: LinearLayout

    @BindView(R.id.llLabel)
    lateinit var llLabel: LinearLayout

    @BindView(R.id.tvLabel)
    lateinit var tvLabel: ExtTextView

    @BindView(R.id.ivIconLeft)
    lateinit var iconLeftImageView: ImageView

    @BindView(R.id.tvContent)
    lateinit var tvContent: ExtTextView

    @BindView(R.id.etContent)
    lateinit var etContent: ExtEditText

    @BindView(R.id.tvHintLayout)
    lateinit var tvHintLayout: ExtTextView

    @BindView(R.id.ivIconRight)
    lateinit var iconRightImageView: ImageView

    @BindView(R.id.tvRightLabel)
    lateinit var tvRightLabel: ExtTextView

    @BindView(R.id.vLine)
    lateinit var vLine: View

    @BindView(R.id.tvError)
    lateinit var errorView: ExtTextView

    // text change
    var textChangeConsumer: ((String) -> Unit)? = null

    var isTextInputLayout: Boolean = false
    var label: String? = ""
    var hint: String? = ""

    // animation view
    var animationLabel: YoYo.YoYoString? = null
    var animationHintLayout: YoYo.YoYoString? = null

    constructor(context: Context) : super(context) {
        initView(context, null)
    }

    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs) {
        initView(context, attrs)
    }

    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
        initView(context, attrs)
    }

    @SuppressLint("CheckResult")
    internal fun initView(context: Context, attrs: AttributeSet?) {
        val view = LayoutInflater.from(context).inflate(R.layout.edit_text_app_view, this)
        ButterKnife.bind(this, view)

        // typed array
        val typedArray = context.theme.obtainStyledAttributes(attrs, R.styleable.EditTextApp, 0, 0)

        // text input layout
        isTextInputLayout = typedArray.getBoolean(R.styleable.EditTextApp_editTextInputLayout, false)

        // update label
        label = typedArray.getString(R.styleable.EditTextApp_editTextAppLabel)
        llLabel.visibility = if (isTextInputLayout) View.INVISIBLE else if (StringUtility.isNullOrEmpty(label)) View.GONE else View.VISIBLE
        tvLabel.text = label
        tvLabel.setTextColor(typedArray.getColor(R.styleable.EditTextApp_editTextAppLabelColor, ContextCompat.getColor(context, R.color.color_label)))

        // hint
        hint = typedArray.getString(R.styleable.EditTextApp_editTextAppHint)

        // hint content text layout
        tvHintLayout.text = hint
        tvHintLayout.visibility = if (isTextInputLayout) View.VISIBLE else View.GONE
        tvHintLayout.setTextColor(typedArray.getColor(R.styleable.EditTextApp_editTextAppLabelColor, ContextCompat.getColor(context, R.color.color_label)))

        // update content
        etContent.setText(typedArray.getString(R.styleable.EditTextApp_editTextAppContent))
        etContent.hint = if (isTextInputLayout) "" else hint

        // text view content
        tvContent.text = typedArray.getString(R.styleable.EditTextApp_editTextAppContent)
        tvContent.hint = if (isTextInputLayout) "" else hint

        // disable view
        val isEnabled = typedArray.getBoolean(R.styleable.EditTextApp_editTextAppEnable, true)
        tvContent.visibility = if (isEnabled) View.GONE else View.VISIBLE
        tvContent.isEnabled = isEnabled
        etContent.visibility = if (isEnabled) View.VISIBLE else View.GONE
        etContent.isEnabled = isEnabled

        // text color & hint color
        etContent.setTextColor(typedArray.getColor(R.styleable.EditTextApp_editTextAppContentColor, ContextCompat.getColor(context, R.color.color_white)))
        etContent.setHintTextColor(typedArray.getColor(R.styleable.EditTextApp_editTextAppHintColor, ContextCompat.getColor(context, R.color.color_white)))

        tvContent.setTextColor(typedArray.getColor(R.styleable.EditTextApp_editTextAppContentColor, ContextCompat.getColor(context, R.color.color_white)))
        tvContent.setHintTextColor(typedArray.getColor(R.styleable.EditTextApp_editTextAppHintColor, ContextCompat.getColor(context, R.color.color_white)))

        // max length
        val maxLength = typedArray.getInt(R.styleable.EditTextApp_android_maxLength, Integer.MAX_VALUE)
        etContent.filters = arrayOf<InputFilter>(InputFilter.LengthFilter(maxLength))

        // icon left
        iconLeftImageView.visibility = View.GONE
        val drawableLeft = typedArray.getDrawable(R.styleable.EditTextApp_editTextAppIconLeft)
        OptionalUtility.with(drawableLeft).doIfPresent {
            iconLeftImageView.setImageDrawable(it)
            iconLeftImageView.visibility = View.VISIBLE
        }

        // icon right
        iconRightImageView.visibility = View.GONE
        val drawableRight = typedArray.getDrawable(R.styleable.EditTextApp_editTextAppIconRight)
        OptionalUtility.with(drawableRight).doIfPresent {
            iconRightImageView.setImageDrawable(it)
            iconRightImageView.visibility = View.VISIBLE
        }

        // label right
        tvRightLabel.visibility = View.GONE
        val labelRight = typedArray.getString(R.styleable.EditTextApp_editTextAppLabelRight)
        tvRightLabel.visibility = if (StringUtility.isNullOrEmpty(labelRight)) View.GONE else View.VISIBLE
        tvRightLabel.text = labelRight

        // input type
        val inputType = typedArray.getInt(R.styleable.EditTextApp_android_inputType, EditorInfo.TYPE_NULL)
        if (inputType != EditorInfo.TYPE_NULL) {
            etContent.inputType = inputType
            // update font
            FontUtility.setCustomizeTypeFace(context, etContent, context.getString(R.string.font_normal_display))
        }

        // ime options
        val imeOptions = typedArray.getInt(R.styleable.EditTextApp_android_imeOptions, EditorInfo.IME_NULL)
        if (imeOptions != EditorInfo.IME_NULL) {
            etContent.imeOptions = imeOptions
        }

        // content lines
        etContent.setLines(typedArray.getInt(R.styleable.EditTextApp_android_lines, 1))

        // show line
        vLine.visibility = if (typedArray.getBoolean(R.styleable.EditTextApp_editTextAppLineVisible, true)) View.VISIBLE else View.INVISIBLE

        // color line
        vLine.setBackgroundColor(typedArray.getColor(R.styleable.EditTextApp_editTextAppLineColor, ContextCompat.getColor(context, R.color.color_label)))

        // text changes
        etContent.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(editable: Editable?) {}
            override fun beforeTextChanged(charSequence: CharSequence?, p1: Int, p2: Int, p3: Int) {}
            override fun onTextChanged(charSequence: CharSequence?, p1: Int, p2: Int, p3: Int) {
                setUpTextInputLayout(etContent.isFocused)
                textChangeConsumer?.invoke("${charSequence.toString().toLowerCase().trim()}")
            }
        })

        // only set focus listener for text input layout
        etContent.setOnFocusChangeListener { _, isFocus -> setUpTextInputLayout(isFocus) }


        // setup text input layout
        setUpTextInputLayout(etContent.isFocused)

        // recycle
        typedArray.recycle()
    }

    fun setUpTextInputLayout(isFocus: Boolean) {
        // this method only perform when it is InputLayout
        if (!isTextInputLayout) return

        // cancel animation
        animationLabel?.stop()
        animationHintLayout?.stop()

        // set color line
        vLine.setBackgroundColor(ContextCompat.getColor(context, if (isFocus) R.color.color_pink else R.color.color_label))

        // animation
        if (!StringUtility.isNullOrEmpty(getContent()) || isFocus) {
            if (llLabel.visibility == View.VISIBLE) return

            llLabel.visibility = View.VISIBLE
            animationLabel = YoYo.with(Techniques.ZoomIn)
                    .duration(DURATION)
                    .onEnd { }
                    .playOn(llLabel)

            animationHintLayout = YoYo.with(Techniques.ZoomOut)
                    .duration(DURATION)
                    .onEnd { tvHintLayout.visibility = View.INVISIBLE }
                    .playOn(tvHintLayout)
        } else {
            animationLabel = YoYo.with(Techniques.ZoomOut)
                    .duration(DURATION)
                    .onEnd { llLabel.visibility = View.INVISIBLE }
                    .playOn(llLabel)

            tvHintLayout.visibility = View.VISIBLE
            animationHintLayout = YoYo.with(Techniques.ZoomIn)
                    .duration(DURATION)
                    .onEnd { }
                    .playOn(tvHintLayout)
        }
    }

    /**
     * set content value
     */
    fun setContent(value: String): EditTextApp {
        etContent.setText(value)
        tvContent.text = value
        return this
    }

    /**
     * show error
     *
     * @param error
     */
    fun setError(error: String?): EditTextApp {
        errorView.text = error
        errorView.visibility = if (StringUtility.isNullOrEmpty(error)) View.INVISIBLE else View.VISIBLE
        return this
    }

    /**
     * show/hide password
     */
    fun onShowOrHidePassword(drawableIdShow: Int = R.drawable.ic_eye, drawableIdHide: Int = R.drawable.ic_eye_disabled): EditTextApp {
        iconRightImageView.setOnClickListener {
            iconRightImageView.setImageResource(if (etContent.transformationMethod != null) drawableIdShow else drawableIdHide)
            etContent.transformationMethod = if (etContent.transformationMethod != null) null else PasswordTransformationMethod()
            TextUtility.setSelection(etContent)
        }
        return this
    }

    /**
     * get content value
     */
    fun getContent(): String = etContent.text.toString()

    /**
     * use to valid null or empty
     *
     * @param context
     */
    fun isEmpty(context: Context, consumer: ((String?) -> Unit)): Boolean {
        if (StringUtility.isNullOrEmpty(getContent())) {
            consumer(context.getString(R.string.error_field_must_not_be_empty))
            return true
        }

        consumer(null)
        return false
    }

    /**
     * use to valid null or empty
     *
     * @param context
     */
    fun isEmpty(context: Context): Boolean = isEmpty(context) { setError(it) }

    /**
     * valid url
     *
     * @param context
     */
    fun isInValidUrl(context: Context, consumer: ((String?) -> Unit)): Boolean {
        if (StringUtility.isNullOrEmpty(getContent())) {
            consumer(context.getString(R.string.error_field_must_not_be_empty))
            return true
        }

        if (!UrlUtility.isValidUrlNetwork(getContent())) {
            consumer(context.getString(R.string.error_url_not_valid))
            return true
        }

        consumer(null)
        return false
    }

    /**
     * valid url
     *
     * @param context
     */
    fun isInValidUrl(context: Context): Boolean = isInValidUrl(context) { setError(it) }

    /**
     * valid email
     *
     * @param context
     */
    fun isInValidEmail(context: Context, consumer: ((String?) -> Unit)): Boolean {
        if (StringUtility.isNullOrEmpty(getContent())) {
            consumer(context.getString(R.string.error_field_must_not_be_empty))
            return true
        }

        if (!EmailUtility.isValidate(getContent())) {
            consumer(context.getString(R.string.error_email_address_not_valid))
            return true
        }

        consumer(null)
        return false
    }

    /**
     * valid email
     *
     * @param context
     */
    fun isInValidEmail(context: Context): Boolean = isInValidEmail(context) { setError(it) }

    /**
     * valid password
     * @param context
     */
    fun isInValidPassword(context: Context, consumer: ((String?) -> Unit)): Boolean {
        if (StringUtility.isNullOrEmpty(getContent())) {
            consumer(context.getString(R.string.error_field_must_not_be_empty))
            return true
        }

        val minPassword = 6
        if (getContent().length < minPassword) {
            consumer(context.getString(R.string.error_min_6_character))
            return true
        }

        if (!AppUtility.isValidPass(getContent())) {
            consumer(context.getString(R.string.error_change_password_required))
            return true
        }

        consumer(null)
        return false
    }

    /**
     * valid password
     * @param context
     */
    fun isInValidPassword(context: Context): Boolean = isInValidPassword(context) { setError(it) }

    /**
     * @param context
     * @param etPassword
     * @return
     */
    fun isInvalidConfirmPassword(context: Context, etPassword: EditTextApp, consumer: ((String?) -> Unit)): Boolean {
        if (StringUtility.isNullOrEmpty(getContent())) {
            consumer(context.getString(R.string.error_field_must_not_be_empty))
            return true
        }

        if (etPassword.getContent().equals(getContent(), false)) {
            consumer(context.getString(R.string.error_confirm_not_match))
            return true
        }

        consumer(null)
        return false
    }

    /**
     * @param context
     * @param etPassword
     * @return
     */
    fun isInvalidConfirmPassword(context: Context, etPassword: EditTextApp): Boolean = isInvalidConfirmPassword(context, etPassword) { setError(it) }
}
