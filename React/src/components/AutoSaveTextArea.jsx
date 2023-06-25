import './../style/style.css'
import {useEffect, useRef, useState} from "react";
import PropTypes from "prop-types";

const AutoSaveTextArea = (props) => {

    const [content, setContent] = useState('');
    const prevItem = useRef(content);

    useEffect(() => {
        if (content !== prevItem.current) {
            setContent(content)
        } else if (content !== props.content) {
            setContent(props.content ? props.content : '')
        }
        prevItem.current = content
    }, [content, props.content])

    const handleBlur = () => {
        if (content !== props.content) {
            props.onSave(content);
        }
    }

    return (
        <textarea maxLength="8000" disabled={props.disabled} className="note_input" onChange={(e) => setContent(e.target.value)}
                  onBlur={() => handleBlur()}
                  value={content}></textarea>
    );
}

AutoSaveTextArea.propTypes = {
    onSave: PropTypes.func.isRequired,
    content: PropTypes.string,
    disabled: PropTypes.bool
}

export default AutoSaveTextArea;