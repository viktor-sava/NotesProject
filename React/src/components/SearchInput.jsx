import './../style/style.css'
import PropTypes from "prop-types";
import {useState} from "react";

const SearchInput = (props) => {
    const [value, setValue] = useState('');

    return (
        <div className="section_search">
            <input onChange={(e) => setValue(e.target.value)}
                   value={value}
                   onBlur={() => props.onSearch(value)}
                   onKeyDown={(e) => {
                       if (e.code === 'Enter') props.onSearch(value);
                   }}
                   className="section_input" type="text" placeholder="Search notes..."/>
            <img className="img " src="/img/search.svg" alt="#" onClick={() => props.onSearch(value)}/>
        </div>
    );
}

SearchInput.propTypes = {
    onSearch: PropTypes.func.isRequired
}

export default SearchInput;