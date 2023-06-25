import {useState} from "react";
import PropTypes from "prop-types";

function TagInput(props) {
    const [tag, setTag] = useState('');

    return (
        <input className="tag_input" type="text" placeholder="Type your tags..." value={tag}
               onChange={(e) => setTag(e.target.value)}
               onKeyDown={(e) => {
                   if (e.code === "Enter" || e.code === "Space") {
                       console.log(tag);
                       props.onAdd(tag);
                       setTag('');
                   }
               }}/>
    );
}

TagInput.propTypes = {
    onAdd: PropTypes.func.isRequired
}

export default TagInput;